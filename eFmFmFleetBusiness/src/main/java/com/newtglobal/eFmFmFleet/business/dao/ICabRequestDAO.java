package com.newtglobal.eFmFmFleet.business.dao;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import com.newtglobal.eFmFmFleet.model.EFmCheckInVehicleTrackingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientBranchPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeRequestMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTravelRequestPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEscortCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmIndicationMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmMultipleLocationTvlReqPO;
import com.newtglobal.eFmFmFleet.model.EFmFmTripTimingMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCapacityMasterPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;

public interface ICabRequestDAO {
	/**
	* Below method's are implemented for Adding ,Modify and Delete functionality on EmployeeTrevelRequest table.
	*
	* @author  Rajan R
	* 
	* @since   2015-05-12 
	*/
	public void save(EFmFmEmployeeTravelRequestPO  eFmFmEmployeeTravelRequestPO);
	public void save(EFmFmEmployeeRequestMasterPO employeeRequestMasterPO);
	public void save(EFmFmAssignRoutePO eFmFmAssignRoutePO);
	public void save(EFmFmTripTimingMasterPO eFmFmTripTimingMasterPO);
	public void update(EFmFmTripTimingMasterPO eFmFmTripTimingMasterPO);
	

	public void update(EFmFmAssignRoutePO eFmFmAssignRoutePO);
	public void update(EFmFmEmployeeTripDetailPO employeeTripDetailPO);
	   

	public void update(EFmFmEmployeeTravelRequestPO  eFmFmEmployeeTravelRequestPO);
	public void delete(EFmFmEmployeeTravelRequestPO  eFmFmEmployeeTravelRequestPO);
	/**
	* The method implemented for handling the duplication while reading travel request from xl sheet. 
	*
	* @author  Rajan R
	* 
	* @since   2015-05-12 
	*/
	public List<EFmFmEmployeeRequestMasterPO> travelRequestExist(String employeeId,
			String tripType, int branchId, String requestType);
	/**
	* The method implemented for travel list for particular day.  
	*
	* @author  Rajan R
	* 
	* @since   2015-05-13 
	*/	
	public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequest(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO);	
	/**
	* The method implemented for getting list of shift time.  
	*
	* @author  Rajan R
	* 
	* @since   2015-05-13 
	*/	
	public List<EFmFmTripTimingMasterPO> listOfShiftTime(String clientId);
	
	public List<EFmFmEmployeeTravelRequestPO> getTodayRequestForParticularEmployee(
			EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest);
	public List<EFmFmEmployeeTravelRequestPO> getparticularRequestDetail(
			EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO);
	public List<EFmFmEmployeeTravelRequestPO> getParticularApproveRequestDetail(
			EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO);
	public List<EFmFmEmployeeTravelRequestPO> getParticularRequestDetail(
			EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO);

	public List<EFmFmAssignRoutePO> gettripForParticularDriver(
			EFmFmAssignRoutePO assignRoutePO);
	public List<EFmFmEmployeeTripDetailPO> getParticularTripAllEmployees(int assignRouteId);
	public List<EFmFmAssignRoutePO> getParticularDriverAssignTripDetail(
			EFmFmAssignRoutePO assignRoutePO);
	public List<EFmFmEmployeeTripDetailPO> getParticularTripParticularEmployees(
			int employeeId, int assignRouteId,int clientId);
	public List<EFmFmEmployeeTripDetailPO> getTodayTripEmployeesDetail(int employeeId,
			int clientId, Date todayDate);
	public List<EFmFmEmployeeTripDetailPO> getAllTodayTripDetails(
			EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest);
	public void update(EFmFmEmployeeRequestMasterPO eFmFmEmployeeRequestMasterPO);
	public List<EFmFmEmployeeTravelRequestPO> getAllTodaysActiveRequests(int clientId);
	public List<EFmFmEmployeeTripDetailPO> getParticularTriprEmployeeBoardedStatus(
			int requestId, int assignRouteId);
	public List<EFmFmEmployeeTravelRequestPO> getAllResheduleRequests(int projectId,
			int branchId);
	public List<EFmFmEmployeeTripDetailPO> getParticularTripNonDropEmployeesDetails(
			int assignRouteId);
	public void deleteParticularTripDetail(int empTripId);
	public List<EFmFmEmployeeTripDetailPO> getrequestStatusFromBranchIdAndRequestId(
			int branchId, int requestId);
	public List<EFmFmEmployeeTripDetailPO> getDropTripAllSortedEmployees(
			int assignRouteId);
	public List<EFmFmEmployeeTravelRequestPO> getAllTodaysRequestForParticularEmployee(
			EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest);
	
	public List<EFmFmEmployeeTravelRequestPO> getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(
			Date date, Time siftTime, String branchId, int userId, String tripType);
	public void save(EFmFmEmployeeTripDetailPO employeeTripDetailPO);
	public List<EFmFmEmployeeRequestMasterPO> getRequestFromRequestMaster(int tripId,
			String branchId);
	public void deleteParticularRequest(int requestId);
	public void deleteParticularRequestFromRequestMaster(int tripId);
	public List<EFmFmEmployeeRequestMasterPO> getAllRequestDetailsFromRequestMasterFromBranchId(
			int branchId);
	public List<EFmFmAssignRoutePO> closeVehicleCapacity(int checkInId, int branchId);
	public List<EFmFmEmployeeRequestMasterPO> getAllRequestFromRequestMasterFprParticularEmployee(
			int userId, String branchId);
	public List<EFmFmAssignRoutePO> getLastRouteDetails(int checkInId, int branchId,
			String tripType);
	public List<EFmFmEmployeeTravelRequestPO> getParticularRequestDetailOnTripComplete(int requestId);
	public List<EFmFmEmployeeTripDetailPO> getAllocatedEmployeeDetail(int userId,
			String branchId, Date todayDate);
	public List<EFmFmEmployeeTravelRequestPO> assignCabRequestToParticularShiftOrRouteEmployees(String requestDate,
			String combinedBranchId, String tripType, Time siftTime, int zoneId);
	//Cab allocation method
    public List<EFmFmEmployeeRequestMasterPO> getParticularEmployeeMasterRequestDetails(
		int branchId, int tripId);
	public List<EFmFmEmployeeTravelRequestPO> getAllParticularRouteRequest(
			int branchId, int zoneId, Time shiftTime);
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleLessCapacity(
			String combinedBranchId, int capacity);
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleLargeCapacity(
			String combinedBranchId, int capacity);
	public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteFromCheckInId(
			int branchId, int zoneId, String reqType, Time shiftTime,
			int checkInId);
	public List<EFmFmAssignRoutePO> getHalfCompletedAssignRoute(int branchId,
			int zoneId, String reqType, Time shiftTime);
	public List<EFmFmEscortCheckInPO> getAllCheckedInEscort(String combinedBranchId);
	public List<EFmFmVehicleMasterPO> getVehicleDetailsFromClientIdForSmallerRoute(
			int branchId, String vehicleId);
	public List<EFmFmEmployeeTripDetailPO> getRequestStatusFromBranchIdAndRequestId(
			String combinedBranchId, int requestId);
	public void deleteParticularRequestFromEmployeeTripDetail(int empTripId);
	public EFmFmVehicleMasterPO getVehicleDetail(int vehicleId);
	public List<EFmFmEmployeeTravelRequestPO> assignCabRequestToParticularShiftEmployees(
			int branchId, String tripType, Time siftTime);
	public  List<EFmFmEmployeeTravelRequestPO> getAllActiveRequests(int branchId);
	public long getAllActivePickUpRequestCounter(int branchId);
	public long getAllActivePickupInProgressEmployeeRequestCounter(String branchId);
	public long getAllActiveNoShowEmployeeRequestCounter(String branchId);
	public long getAllActiveBoardedEmployeeRequestCounter(String branchId);
	public long getAllActiveDropRequestCounter(String branchId);
	public long getAllActiveDropedEmployeeRequestCounter(String branchId);
	public long getAllActiveDropNoShowEmployeeRequestCounter(String branchId);
	public long getAllActiveDropInProgressEmployeeRequestCounter(String branchId);
	public long getAllPickupScheduleActiveRequests(String branchId);
	public long getAllDropScheduleActiveRequests(String branchId);
	public List<EFmFmEmployeeTravelRequestPO> getAnotherActiveRequestDetail(
			EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO);
	public List<EFmFmEmployeeTravelRequestPO> getAnotherActiveRequestForNextDate(
			EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO);
	public List<EFmFmEmployeeTravelRequestPO> particularEmployeeRequestFromEmpId(
			String branchId, String employeeId);
	public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequestForAdminShiftWise(
			EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO);
	public long getAllActiveDropOrPickupRequestCounterForGuest(String branchId);
	
	public long getAllActiveNoShowGuestRequestCounter(String branchId);
	public long getAllActiveBoardedGuestRequestCounter(String branchId);
	public long getAllScheduleActiveRequestsForGuest(String branchId);
	public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequestByShiftWice(int branchId,String requestDate,Time shiftTime);
	public List<EFmFmEmployeeTravelRequestPO> listOfAdhocAndGuestTravelRequests(
			int branchId);
	public List<EFmFmEmployeeTravelRequestPO> getAllDropScheduleActiveRequestsDetails(
			String branchId);
	public List<EFmFmEmployeeTripDetailPO> getAllActiveDropedEmployeeRequestsDetails(
			String branchId);
	public List<EFmFmEmployeeTripDetailPO> getAllActiveDropNoShowEmployeeRequestsDetails(
			String branchId);
	public List<EFmFmEmployeeTravelRequestPO> getAllPickupScheduleActiveRequestsDetails(
			String branchId);
	public List<EFmFmEmployeeTripDetailPO> getAllActivePickupBoardedEmployeeRequestsDetails(
			String branchId);
	public List<EFmFmEmployeeTripDetailPO> getAllActivePickupNoShowEmployeeRequestsDetails(
			String branchId);
	public List<EFmFmEmployeeTravelRequestPO> getAllActiveDropRequestDetails(
			int branchId);
	public List<EFmFmEmployeeTravelRequestPO> getAllActivePickUpRequestDetails(
			int branchId);
	public List<EFmFmEmployeeTripDetailPO> getNonDropTripAllSortedEmployees(
			int assignRouteId);
	public List<EFmFmEmployeeTripDetailPO> getEmployeeLiveTripDetailFromUserId(
			int userId, int branchId);
	public List<EFmFmTripTimingMasterPO> getParticularShiftTimeDetail(int branchId,
			Time shiftTime);
	public List<EFmFmTripTimingMasterPO> listOfShiftTimeByTripType(String branchId,
			String tripType);
	public List<EFmFmEmployeeRequestMasterPO> getAllRequestDetailsFromRequestMasterFromBranchIdByTripType(
			int branchId, String tripType);
	public List<EFmFmEmployeeTravelRequestPO> getAllScheduleActiveGuestRequestsDetails(
			String branchId);
	public List<EFmFmEmployeeTripDetailPO> getAllActiveBoardedOrDroppedEmployeeRequestsDetailsForGuest(
			String branchId);
	public List<EFmFmEmployeeTravelRequestPO> getAllActiveGuestRequestsDetails(
			String branchId);
	public List<EFmFmEmployeeTripDetailPO> getAllActiveGuestNoShowRequestsDetails(
			String branchId);
	public List<EFmFmEmployeeTravelRequestPO> particularEmployeePickupRequestFromUserId(
			int branchId, int userId, String tripType);
	public List<EFmFmEmployeeTravelRequestPO> deleteCurentRequestfromTraveldesk(
			int branchId, int tripId);
	public List<EFmFmTripTimingMasterPO> getShiftTimeDetailFromShiftTimeAndTripType(
			String combinedBranchId, Time shiftTime, String tripType);
	public List<EFmFmTripTimingMasterPO> getParticularShiftTimeDetailByTripType(
			String branchId, Time shiftTime, String tripType);	
	public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequestByShiftTripType(int branchId, Time shiftTime, String tripType,	String todaysDate);
	public List<EFmFmClientBranchPO> getBranchDetails(int branchId);
	public List<EFmFmEmployeeTravelRequestPO> getparticularRequestwithShiftTime(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO);
	public List<EFmFmAssignRoutePO> getCreatedAssignRoute(int branchId, String reqType, Time shiftTime);
	public List<EFmFmEmployeeTripDetailPO> getDropTripByAlgoRouteSortedEmployees(int assignRouteId);
	public void deleteParticularTripDetailByRouteId(int routeId);
	public List<EFmFmEmployeeTravelRequestPO> getparticularEmployeeRequest(String todaysDate, String employeeId,String tripType, int branchId, String shiftTime);
	public EFmFmEmployeeTripDetailPO ParticularTripDetail(int empTripId);
	public List<EFmFmEmployeeTripDetailPO> getparticularEmployeeTripDetails(String todaysDate, String employeeId,String tripType, int branchId, String shiftTime);
	public List<EFmFmEmployeeRequestMasterPO> getParticularRequestDetailFromUserIdAndTripType(
			int userId, int branchId, String tripType);
	public List<Time> listOfAllShiftTimesFromTravelDesk(String branchId,String tripType);
	public List<EFmFmEmployeeTripDetailPO> getParticularTripEmployeeRequestDetails(
			int empTripId, int requestId, int assignRouteId);
	public List<EFmFmEmployeeTripDetailPO> getTripAllEmployeesFromAssignRouteIdBranchId(
			int assignRouteId, int branchId);
	public List<EFmFmEmployeeTripDetailPO> getCoPassenger(int assignRouteId,
			int requestId);
	public List<EFmFmEmployeeRequestMasterPO> getActiveRequestDetailsFromBranchIdAndUserId(int branchId, int userId);
	public List<EFmFmVehicleCheckInPO> getAllCheckedInVehiclesForSpecificCapacity(String combinedBranchId, int capacity);
	public List<EFmFmEmployeeTravelRequestPO> assignCabToParticularShiftOrDateEmployees(String requestDate, int branchId,
			String tripType, Time siftTime);
	public List<EFmFmEmployeeTravelRequestPO> assignCabToParticularShiftDateOrRouteEmployees(String requestDate, String combinedBranchId,
			String tripType, Time siftTime, int zoneId);
	public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteByDate(String requestDate, int branchId, int zoneId,
			String reqType, Time shiftTime);
	public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteFromCheckInIdByDate(String requestDate, int branchId,
			int zoneId, String reqType, Time shiftTime, int checkInId);
	public List<EFmFmEmployeeTravelRequestPO> getAllParticularRouteRequestByDate(String requestDate, int branchId, int zoneId,
			Time shiftTime);
	public List<EFmFmEmployeeTripDetailPO> getDropTripAllSortedNonDropEmployees(int assignRouteId);
	public List<EFmFmEmployeeTripDetailPO> getAllocatedEmployeeDetailFromUserIdAndBranchId(int userId, int branchId);
    public List<EFmFmEmployeeTravelRequestPO> getAllUpComingRequestForParticularEmployeeTripBased(int branchId, int userId,
            String tripType);
    public List<EFmFmEmployeeTravelRequestPO> getParticularRequestFromRequestDateAndUserId(Date todayDate,String tripType, int userId,
            int branchId);
    public void update(EFmCheckInVehicleTrackingPO eFmCheckInVehicleTrackingPO);
    public List<EFmFmEmployeeTravelRequestPO> getParticularEmployeeLastRequestFromUserId(String tripType, int userId,
            int branchId);
    public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequestByShiftWiceForNormalAndAdhocRequests(int branchId,
            String requestDate, Time shiftTime);
	public List<EFmFmTripTimingMasterPO> getParticularShiftTimeDetailByShiftId(int branchId, int shifId);
    public List<EFmFmEmployeeTravelRequestPO> listOfAdhocReservationsForGuestTravelRequests(int branchId);
	public List<EFmFmEmployeeTravelRequestPO> particularDateRequestForEmployees(Date date, int branchId, int userId,
			String tripType);
	public List<EFmFmEmployeeTravelRequestPO> listOfGuestTravelRequests(int branchId);
	public List<EFmFmEmployeeTravelRequestPO> getNextWeekRequestForParticularEmployee(
			EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest);
	public List<EFmFmEmployeeRequestMasterPO> getActiveEmplyeeRequest(int branchId, int userId, String tripType);
	public List<EFmFmEmployeeTravelRequestPO> assignCabToPickupShiftOrDateEmployees(String requestDate, String combinedBranchId,
			String tripType, Time siftTime);
	public List<EFmFmEmployeeTravelRequestPO> assignCabToDropShiftOrDateEmployees(String requestDate, String combinedBranchId,
			String tripType, Time siftTime);

	public List<EFmFmTripTimingMasterPO> getShiftIdUsingshiftTime(int branchId, String tripType, Time siftTime);
	public List<EFmFmEmployeeTravelRequestPO> listOfGuestAndAdhocRequestTravelRequests(int branchId);
	public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForNormalAndAdhoc(String branchId, String requestDate,
			Time shiftTime,String tripType);
	public List<EFmFmEmployeeTravelRequestPO> getGuestAndAdhocTravelRequestsForGivendate(String branchId, String requestDate);
	public List<EFmFmEmployeeTravelRequestPO> getAllRequestForParticularEmployeeBasedOnMobileNumber(String branchId,String mobileNumber, int startPgNo, int endPgNo);
	public List<EFmFmTripTimingMasterPO> listOfShiftTimeForEmployees(String branchId);
	public List<EFmFmTripTimingMasterPO> listOfShiftTimeByTripTypeForEmployees(int branchId, String tripType);
	public List<EFmFmEmployeeTravelRequestPO> getAllUpComingRequestForParticularEmployee(int branchId, int userId,
			String tripType);
	public List<EFmFmEmployeeRequestMasterPO> getAllActiveRequestDetailsFromRequestMasterdByBranchIdAndTripType(String branchId,
			String tripType);
	public List<EFmFmEmployeeRequestMasterPO> getActiveRequestDetailsFromBranchIdAndUserIdForAnEmployee(String branchId,
			int userId);
	public long getAllActivePickUpRequestCounterForTodays(String branchId, String todayDate);
	public long getAllActiveDropRequestCounterForTodays(String branchId, String todayDate);
	List<EFmFmEmployeeTravelRequestPO> getAllActivePickUpRequestDetailsForToday(String branchId, String requestDate);
	List<EFmFmEmployeeTravelRequestPO> getAllActiveDropRequestDetailsForToday(String branchId, String requestDate);
	public List<EFmFmAssignRoutePO> getStartedVehicleDetailFromVehicleNumber(int checkInId, String branchId);
	
	public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteByRouteName(String requestDate, int branchId, int zoneId,
			String tripType, Time shiftTime, String string);
	public List<EFmFmEmployeeTripDetailPO> getParticularTripAllEmployeesDesc(int assignRouteId);
	public List<EFmFmEmployeeTripDetailPO> getRouteEmployeeStatus(int assignRouteId);
	public List<EFmFmEmployeeTripDetailPO> getParticularTripEmployees(int assignRouteId);
	public List<EFmFmEmployeeRequestMasterPO> getEmployeeRequestDetails(String branchId, int UserId);
	public List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestValidation(Date date, Date toDate, int branchId, int userId,
			String tripType);

	public List<EFmFmTripTimingMasterPO> getCeilingShiftTimeDetailByShiftId(int branchId, int shiftId);
	public List<EFmFmEmployeeTravelRequestPO> getEmployeeRequestCeilingCount(int branchId, String requestDate,
			String shiftTime, String tripType);
	public List<EFmFmEmployeeRequestMasterPO> getEmployeeRequestCeiling(int branchId, String requestDate, String shiftTime,
			String tripType);
	public List<EFmFmEmployeeRequestMasterPO> getParticularRequestDetailFromUserIdByRead(int userId, int branchId,
			String tripType);
	public List<EFmFmEmployeeTravelRequestPO> pastRequestByTravelMaster(int tripId);
	public List<EFmFmEmployeeRequestMasterPO> getRequestMasterDetailsByTripId(int tripId);
	
	public List<EFmFmEmployeeTravelRequestPO> getParticularActiveRequestDetail(int userId, String tripType, String requestDate,
			Time shiftTime);
	
	public List<EFmFmEmployeeTravelRequestPO> employeeTravelledRequestValidation(Date date, Date toDate, int branchId,
			int userId, String tripType);
	public List<EFmFmVehicleCheckInPO> getAllCheckedInDummyVehicles(String combinedBranchId);
	public List<EFmFmEmployeeTripDetailPO> getAllWithOutNoShowDropTripEmployees(int assignRouteId);
	public List<EFmFmEmployeeTripDetailPO> getAllWithOutNoShowPickupTripEmployees(int assignRouteId);
	
	public List<EFmFmEmployeeTravelRequestPO> assignCabByLocationFlg(String requestDate, String combinedBranchId, String tripType,
			Time siftTime);
	public List<EFmFmEmployeeTravelRequestPO> assignCabToParticularShiftDateByLocationFlg(String requestDate, String combinedBranchId,
			String tripType, Time siftTime, int zoneId,String locationFlg);
	public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteByLocationFlg(String tripAssignDate, int branchId, int zoneId,
			String reqType, Time shiftTime, String locationFlg);
	public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteByDateWithNormalFlg(String tripAssignDate, int branchId,
			int zoneId, String reqType, Time shiftTime);		
	public void save(EFmFmMultipleLocationTvlReqPO eFmFmMultipleLocationTvlReqPO);
	public void modify(EFmFmMultipleLocationTvlReqPO eFmFmMultipleLocationTvlReqPO);
	public List<EFmFmMultipleLocationTvlReqPO> getLocationTravelDetails(int locTvlId,int branchId);
	public List<EFmFmMultipleLocationTvlReqPO> getLocationTravelDetailsByRequestId(int requestId);
	public int getRequestId(EFmFmEmployeeRequestMasterPO employeeRequestMasterPO);
	public List<EFmFmEmployeeTripDetailPO> getlistOfRequestByAssignedId(int assignRouteId);
	public List<EFmFmEmployeeTravelRequestPO> getEmpMultipleTravelledRequest(int branchId, String requestDate);
	public long getAllocatedCountReuestsByShiftWice(int branchId, String requestDate, Time shiftTime, String tripType);
	public List<EFmFmEmployeeTravelRequestPO> assignCabRequestToParticularShiftOrRouteEmployeesByLocationFlg(
			String requestDate, String combinedBranchId, String tripType, Time siftTime, int zoneId);
	
	public List<EFmFmEmployeeTravelRequestPO> getListOfApprovalPendingRequestForUser(String branchId, int userId,
			String approvalFlg);
	
	public List<EFmFmEmployeeTravelRequestPO> getListOfApprovalPendingRequest(String branchId, int approvalUserId,
			String approvalFlg);
	public List<EFmFmEmployeeRequestMasterPO> getListOfBulkApprovalPendingRequest(int branchId, int approvalUserId,
			String approvalFlg);
	public List<EFmFmEmployeeTravelRequestPO> employeeRequestFromEmpIdByLocationFlg(String branchId, String employeeId);
	
	public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForLocationFlg(int branchId, String requestDate,
			Time shiftTime, String tripType);
	public List<EFmFmEmployeeTravelRequestPO> getAllNormalAndAdhocRequest(int branchId, String requestDate, String tripType);
	public List<EFmFmIndicationMasterPO> getIndicationDetailsExist(int branchId, String alertTypeRequest, 
			String alertFunctionlityType,String levelType ,String tiggerTime,int userId);
	public List<EFmFmIndicationMasterPO> getAllIndicationDetails(int branchId,String isActive);
	public void save(EFmFmIndicationMasterPO eFmFmIndicationMasterPO);
	public void update(EFmFmIndicationMasterPO eFmFmIndicationMasterPO);
	public List<EFmFmIndicationMasterPO> getAllAlertFunctionlityType(int branchId);	
	public List<EFmFmIndicationMasterPO> getAllLevelType(int branchId,String getAllAlertFunctionlityType);	
	public List<EFmFmIndicationMasterPO> getAllAlertFunctionlityTypeByLevelType(int branchId,String getAllAlertFunctionlityType,String levelType);
	public List<EFmFmIndicationMasterPO> getAllIndicationById(int branchId, int indicationId);
	
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhoc(String branchId, String requestDate,
			String tripType);
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceBothPickAndDropForNormalAndAdhoc(String branchId,
			String requestDate);
	public List<EFmFmEmployeeTravelRequestPO> routingByAreaSequencing(String requestDate, int branchId,String tripType,
			Time siftTime);
	
	public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForNormalAndAdhocByManager(int approvalUserId,
			String branchId, String requestDate, Time shiftTime, String tripType);
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhocByManager(String branchId, int approvalUserId,
			String requestDate, String tripType);
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceBothPickAndDropForNormalAndAdhocByManager(String branchId,
			int approvalUserId, String requestDate);

	public List<EFmFmEmployeeTripDetailPO> getAllocatedEmployeeDetailYetToBoard(int userId, int branchId, Date todayDate);
	public List<EFmFmEmployeeTripDetailPO> getEmployeeLiveTripDetailFromUserIdBeforeOnBoard(int userId, int branchId);
	public List<EFmFmEmployeeRequestMasterPO> getParticularRequestDetailFromUserIdByShiftTime(int userId, int branchId,
			String tripType, Time shiftTime);
	public List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestValidationByGuest(Date date, Date toDate, int branchId,
			int userId, String tripType, Time shiftTime);
	public List<EFmFmEmployeeTravelRequestPO> employeeTravelledRequestValidationByGuest(Date date, Date toDate, int branchId,
			int userId, String tripType, Time shiftTime);
	public List<EFmFmEmployeeTripDetailPO> getAllocatedTripDetails(int userId, String branchId);
	public List<EFmFmEmployeeTravelRequestPO> getRequestByShiftWiceForNormalAndAdhocWithoutTripType(String branchId,
			String requestDate, Time shiftTime);
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhocByManagerWithoutTripType(String branchId,
			int approvalUserId, String requestDate, Time shiftTime);
	public List<EFmFmEmployeeTravelRequestPO> tripRequestDetailsByTravelMaster(int requestId);
	public List<EFmFmEmployeeTripDetailPO> getParticularTripModificationDetails(int assignRouteId);
	public List<EFmFmEmployeeTripDetailPO> getAlgoDropRequestEmployees(int assignRouteId);
	
	
	public Integer getBiggestVehicleTypeCapacity(String branchId);
	public Integer getLowestVehicleTypeCapacityLessThan(String branchId, int capacity);
	public Integer getGreatestVehicleTypeCapacity(String branchId, int capacity);
	public List<EFmFmVehicleCheckInPO> getAllDummyCheckedInVehiclesForGreatestCapacity(String branchId, int capacity);
	public List<EFmFmVehicleCheckInPO> getAllDummyCheckedInVehiclesForLessCapacity(String branchId, int capacity);
	
	public Integer getLowestVehicleTypeCapacity(String branchId);
	public List<EFmFmEmployeeTripDetailPO> getLastDropEmployeeDetail(int assignRouteId);
	public void save(EFmCheckInVehicleTrackingPO eFmCheckInVehicleTrackingPO);
	//public List<EFmFmVehicleCapacityMasterPO> getAllVehicleTypeCapacityWise(String branchId);
	public List<EFmFmEmployeeRequestMasterPO> getEmplyeeRequestsForSameDateAndShiftTime(Date date, Time siftTime,
			String branchId, int userId, String tripType);
	public List<EFmFmEmployeeRequestMasterPO> getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(Date date, Time siftTime,
			String branchId, int userId, String tripType);
	public List<Integer> getAllCapacitiesOfTheVehicles(String branchId);
	public List<EFmFmEmployeeRequestMasterPO> getBulkRequestByTripId(int tripId);
	public List<EFmFmVehicleCheckInPO> getParticularCapacityDummyCheckedInVehicle(String branchId, int capacity);
	public long assignCabCountToParticularShiftDateOrRouteEmployees(String requestDate,
			String branchId, String tripType, Time siftTime, int zoneId);
	public long assignCabCountToParticularShiftDateByLocationFlg(String requestDate,
			String branchId, String tripType, Time siftTime, int zoneId, String locationFlg);
	public EFmFmVehicleCheckInPO getCheckedInSingleVehicleForSpecificCapacity(String branchId, int capacity);
	public EFmFmVehicleCheckInPO getCheckedInSingleVehicleForSpecifiLessCapacity(String branchId, int capacity);
	public EFmFmVehicleCheckInPO getCheckedInSingleVehicleForSpecificGreatestCapacity(String branchId, int capacity);
	
	public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForNormalAndAdhoc(String branchId, String requestDate,
			Time shiftTime,String tripType,int startPgNo,int endPgNo);
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhoc(String branchId, String requestDate,
			String tripType,int startPgNo,int endPgNo);
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceBothPickAndDropForNormalAndAdhoc(String branchId,
			String requestDate,int startPgNo,int endPgNo);
	
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhocByManager(String branchId, int approvalUserId,
			String requestDate, String tripType,int startPgNo,int endPgNo);
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceBothPickAndDropForNormalAndAdhocByManager(String branchId,
			int approvalUserId, String requestDate,int startPgNo,int endPgNo);	
	public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForNormalAndAdhocByManager(int approvalUserId,
			String branchId, String requestDate, Time shiftTime, String tripType,int startPgNo,int endPgNo);
	
	public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForLocationFlg(String branchId, String requestDate,Time shiftTime, String tripType,int startPgNo,int endPgNo);
	public List<EFmFmEmployeeTravelRequestPO> getEmpMultipleTravelledRequest(String branchId, String requestDate,int startPgNo,int endPgNo);
	public List<EFmFmEmployeeTravelRequestPO> getGuestAndAdhocTravelRequestsForGivendate(String branchId, String requestDate,int startPgNo,int endPgNo);
	public List<EFmFmEmployeeTravelRequestPO> getListOfApprovalPendingRequest(String branchId, int approvalUserId,
			String approvalFlg,int startPgNo,int endPgNo);
	 public List<EFmFmEmployeeTravelRequestPO> listOfAdhocReservationsForGuestTravelRequests(String branchId,int startPgNo,int endPgNo);
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForPickupRouting(String requestDate,
            String branchId, String tripType, Time siftTime);
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForDropRouting(String requestDate,
            String branchId, String tripType, Time siftTime);
	public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteForAutoRouting(String tripAssignDate, int branchId,
			String reqType, Time shiftTime, int checkInId);
}
