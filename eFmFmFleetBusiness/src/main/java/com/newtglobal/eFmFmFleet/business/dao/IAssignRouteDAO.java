package com.newtglobal.eFmFmFleet.business.dao;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.newtglobal.eFmFmFleet.model.EFmCheckInVehicleTrackingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmActualRoutTravelledPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmLiveRoutTravelledPO;

public interface IAssignRouteDAO {
	
	/*
	 * employee Travelled route entry data
	 */
	public void save(EFmFmActualRoutTravelledPO  actualRoutTravelledPO);
	public void update(EFmFmActualRoutTravelledPO  actualRoutTravelledPO);
	public void update(EFmFmLiveRoutTravelledPO  actualRoutTravelledPO);
	public void save(EFmFmLiveRoutTravelledPO  actualRoutTravelledPO);
	
	
	public List<EFmFmAssignRoutePO> getAllTodaysTrips(EFmFmAssignRoutePO assignRoutePO);
	public List<EFmFmAssignRoutePO> closeParticularTrips(
			EFmFmAssignRoutePO assignRoutePO);
	public void update(EFmFmAssignRoutePO eFmFmAssignRoutePO);
	public List<EFmFmAssignRoutePO> getAllLiveTrips(EFmFmAssignRoutePO assignRoutePO);
	public List<EFmFmAssignRoutePO> getAllRoutesOfParticularZone(
			EFmFmAssignRoutePO assignRoutePO);
	public List<EFmFmAssignRoutePO> getAllOnlyAssignedTrips(
			EFmFmAssignRoutePO assignRoutePO);
	public List<EFmFmActualRoutTravelledPO> getEtaAndDistanceFromAssignRouteId(EFmFmActualRoutTravelledPO actualRouteTravelledPO);
	
	public List<EFmFmAssignRoutePO> getAllTodaysCompletedTrips(
			EFmFmAssignRoutePO assignRoutePO);
	public List<EFmFmAssignRoutePO> getTripCountByDate(Date fromDate, Date toDate,
			String tripType, int clientId);
	public List<EFmFmAssignRoutePO> getAllTripDetails(EFmFmAssignRoutePO assignRoutePO);
	public List<EFmFmAssignRoutePO> getAllTripByDate(Date fromDate, Date toDate,
			String combinedBranchId);
	public List<EFmFmAssignRoutePO> getAllActiveTrips(EFmFmAssignRoutePO assignRoutePO);
	public void deleteParticularAssignRoute(int assignRouteId);
	public void deleteParticularActualTravelled(int travelId);
	public List<EFmFmAssignRoutePO> getAllRoutesOfParticularBranch(
			EFmFmAssignRoutePO assignRoutePO);
	public void save(EFmFmAssignRoutePO eFmFmAssignRoutePO);
	public List<EFmFmAssignRoutePO> getAllRoutesBasedOnTripTypeAndShiftTime(
			EFmFmAssignRoutePO assignRoutePO);
	public List<EFmFmAssignRoutePO> getAllRoutesInsideZone(int branchId, int zoneId);
	public long getPickupVehiclesOnRoadCounter(int branchId);
	public long getDropVehiclesOnRoadCounter(int branchId);
	public long getAllVehiclesOnRoadCounter(String branchId);
	public List<EFmFmActualRoutTravelledPO> getCabLocationFromAssignRouteId(
			EFmFmActualRoutTravelledPO actualRouteTravelledPO);
	public List<EFmFmActualRoutTravelledPO> getLastEtaFromAssignRouteId(
			EFmFmActualRoutTravelledPO actualRouteTravelledPO);
	public List<EFmFmAssignRoutePO> getAllBucketClosedRoutes(
			EFmFmAssignRoutePO assignRoutePO);
	public List<EFmFmAssignRoutePO> getAllStartedRoutes(
			EFmFmAssignRoutePO assignRoutePO);
	public List<EFmFmAssignRoutePO> getAllClosedRoutes(String tripType,
			Time shiftTime, int branchId);
	public List<EFmFmAssignRoutePO> getAllOpenBucketRoutes(
			EFmFmAssignRoutePO assignRoutePO);
	public List<EFmFmAssignRoutePO> getAllTripsTravelledAndPlannedDistanceByDate(
			Date fromDate, Date toDate, String combinedBranchId, int vendorId);
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByDate(Date fromDate,
			Date toDate, String combinedBranchId);
	public List<EFmFmAssignRoutePO> getAllTripsTravelledAndPlannedDistanceByDateAndVehicle(
			Date fromDate, Date toDate, int branchId, int vehicleId);
	public List<EFmFmAssignRoutePO> getAllTripsTravelledAndPlannedDistanceByAllVendor(
			Date fromDate, Date toDate, String combinedBranchId);
	public String getVendorNameTravelledAndPlannedDistanceByAllVendor(Date fromDate,
			Date toDate, int branchId);
	public List<Date> getAllTripsDistinctDates(Date fromDate, Date toDate, String combinedBranchId,
			String tripType);
//All function Counts for For Date Wice
	public long getAllTripsCountByDate(Date fromDate, Date toDate, int branchId,
			String tripType);
	public List<EFmFmAssignRoutePO> getAllTripsByDate(Date fromDate, Date toDate, int branchId,
			String tripType);
	public long getAllEmployeesCountByDate(Date fromDate, Date toDate, int branchId,
			String tripType);
	public long getNoShowEmployeesCountByDate(Date fromDate, Date toDate,
			int branchId, String tripType);
	public long getPickedUpEmployeesCountByDate(Date fromDate, Date toDate,
			int branchId, String tripType);
	
	
	public long getAllDelayTripsCountByDate(Date fromDate, Date toDate, int branchId,
			String tripType);
	public long getAllDelayTripsBeyondLoginTimeCountByDate(Date fromDate, Date toDate,
			int branchId, String tripType);
	public List<Time> getAllTripsByShift(Date fromDate, Date toDate, int branchId,
			String tripType, Time shiftTime);
	
	public List<Date> getAllTripsByByVendorId(Date fromDate, Date toDate,
			int branchId, String tripType, int vendorId);
	
	//All function Counts Shift Wice
	public long getAllTripsCountByShift(Date fromDate, Date toDate, int branchId,
			String tripType,Time shiftWice);
	public List<EFmFmAssignRoutePO> getAllTripsDetailsByShift(Date fromDate, Date toDate, String combinedBranchId,
			String tripType,Time shiftWice);
	public long getAllEmployeesCountByShift(Date fromDate, Date toDate, int branchId,
			String tripType,Time shiftWice);
	public long getNoShowEmployeesCountByShift(Date fromDate, Date toDate,
			int branchId, String tripType,Time shiftWice);
	public long getPickedUpEmployeesCountByShift(Date fromDate, Date toDate,
			int branchId, String tripType,Time shiftWice);
	public List<Time> getAllTripsByShiftByVendorId(Date fromDate, Date toDate,
			int branchId, String tripType, int vendorId,Time shiftTime);
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByIdAndName(
			Date fromDate, Date toDate, int branchId, String tripType,Time shiftTime);
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByEmployeeId(
			Date fromDate, Date toDate, int branchId, String tripType,
			Time shiftTime, String employeeId);
	
	public List<EFmFmAssignRoutePO> getAllTripsDetailsByShiftByVendor(Date fromDate,
			Date toDate, int branchId, String tripType, Time shiftTime,
			int vendorId);
	public List<EFmFmAssignRoutePO> getAllTripsDetailsByVendorWiseOnly(Date fromDate,
			Date toDate, int branchId, String tripType, int vendorId);	
	
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByEmployeeIdDateWise(
			Date fromDate, Date toDate, String combinedBranchId, String tripType,
			String employeeId);
	public List<Time> getAllTripsByAllShifts(Date fromDate, Date toDate, int branchId,
			String tripType);
	public List<Time> getAllTripsByAllShiftsForVendor(Date fromDate, Date toDate,
			int branchId, String tripType, int vendorId);
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByDateWise(
			Date fromDate, Date toDate, int branchId, String tripType);
	public List<EFmFmAssignRoutePO> getAllTripsVehicleDetailsByVehicleNumber(
			Date fromDate, Date toDate, String combinedBranchId, String vehicleNumber);
	public List<EFmFmAssignRoutePO> getAllTripsVehicleKMDetailsByShiftTime(
			Date fromDate, Date toDate, String combinedBranchId, Time shiftTime);
	public List<EFmFmEmployeeTripDetailPO> getAllMessageEmployeesMessageDetailsByDate(
			Date fromDate, Date toDate, String combinedBranchId);
	public long getUsedFleetByByVendorId(Date fromDate, Date toDate,
			int branchId, String tripType, int vendorId);
	public long getNoShowEmployeesCountByDateByVendor(Date fromDate, Date toDate,
			int branchId, String tripType, int vendorId);
	public long getPickedUpEmployeesCountByDateByVendor(Date fromDate, Date toDate,
			int branchId, String tripType, int vendorId);
	public long getPickedUpEmployeesCountByShiftByVendor(Date fromDate, Date toDate,
			int branchId, String tripType, Time shiftTime, int vendorId);
	public long getNoShowEmployeesCountByShiftByVendor(Date fromDate, Date toDate,
			int branchId, String tripType, Time shiftTime, int vendorId);
	public long getUsedFleetByByVendorIdByShift(Date fromDate, Date toDate,
			int branchId, String tripType, int vendorId, Time shiftTime);
	public long getAllEmployeesCountByDateByVendorId(Date fromDate, Date toDate,
			int branchId, String tripType, int vendorId);
	public long getAllEmployeesCountByShiftByVendorId(Date fromDate, Date toDate,
			int branchId, String tripType, Time shiftTime, int vendorId);
	public List<EFmFmAssignRoutePO> getTodayTripByShift(Date fromDate, Date toDate, String tripType, String ShifTime,
			int branchId);
	public List<EFmFmAssignRoutePO> getExportTodayTrips(Date fromDate, Date toDate, String tripType, String ShifTime,String branchId);
	public List<EFmFmAssignRoutePO> getAllEscortRequiredTripsByDate(Date fromDate,
			Date toDate, String combinedBranchId);
	public List<Date> getAllTripsByDistinctDates(Date fromDate, Date toDate,
			String combinedBranchId);
	public long getPickedUpOrDroppedEmployeesCountByDate(Date fromDate, Date toDate,
			int branchId, String tripType);
	public List<ArrayList> getAllTripsByDistinctDatesAndDeriverId(Date fromDate,
			Date toDate, int branchId);
	public List<EFmFmAssignRoutePO> getAllTripByDateByDeriverId(Date fromDate,
			Date toDate, int branchId, int driverId);
	public List<EFmFmAssignRoutePO> getAllTripsByDatesAndDriverId(Date fromDate,
			Date toDate, String combinedBranchId, int driverId);
	public List<EFmFmAssignRoutePO> getAllBucketClosedAndNotStartedRoutes(EFmFmAssignRoutePO assignRoutePO);
	public List<EFmFmAssignRoutePO> getAllTripForTravelTimeReports(Date fromDate, Date toDate, String tripType, String combinedBranchId);
	public List<EFmFmAssignRoutePO> getAllVehicleDistance(Date fromDate, Date toDate, int branchId, int vehicleId);
	   public List<EFmFmEmployeeTripDetailPO> getParticuarEmployeesTravelledDetailByEmployeeId(Date fromDate, Date toDate,
	            int branchId, String tripType, String employeeId);
	    public List<EFmFmEmployeeTripDetailPO> getParticuarEmployeesTravelledDetailByEmployeeIdAndBranchId(Date fromDate, Date toDate,
	            String combinedBranchId, String employeeId);
	    public List<EFmFmAssignRoutePO> getAllEdiTripByBranchId(String branchId);
	    public List<EFmCheckInVehicleTrackingPO> vehicleTrackingAfterCheckIn(String branhId, int checkInId);
		public List<EFmFmAssignRoutePO> getLastCompletedTripByDriver(int checkInId, int branchId);
		public List<EFmFmAssignRoutePO> getAllTodaysTripsWithOutDummyVehicle(EFmFmAssignRoutePO assignRoutePO);
		public List<EFmFmAssignRoutePO> getDuplicateTripAllocationCheck(int checkInId, String branchId);
		public List<EFmFmAssignRoutePO> getTripAllocatedRoute(int checkInId, int branchId);
			/*OTA & OTA Report*/
		public List<Date> getAllTripsDistinctDatesOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType,String requestType);
		public long getUsedFleetByByVendorIdOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType, int vendorId,String requestType);
		public long getUsedFleetByByVendorIdByShiftOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType, int vendorId,Time shiftTime,String requestType);
		public long getAllTripsCountByDateOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType,String requestType);
		public long getAllDelayTripsCountByDateOTA(Date fromDate, Date toDate, int branchId, String tripType,String requestType);
		public long getAllDelayTripsBeyondLoginTimeCountByDateOTA(Date fromDate, Date toDate, int branchId, String tripType,String requestType);
		public long getAllEmployeesCountByDateOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType,String requestType);
		public long getAllEmployeesCountByDateByVendorIdOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType, int vendorId,String requestType);
		public long getNoShowEmployeesCountByDateOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType,String requestType);
		public long getNoShowEmployeesCountByDateByVendorOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType, int vendorId,String requestType);
		public long getPickedUpEmployeesCountByDateOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType,String requestType);
		public long getPickedUpEmployeesCountByDateByVendorOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType,int vendorId,String requestType);
		public List<EFmFmAssignRoutePO> getAllTripsByDateOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType,String requestType);
		public long getAllTripsCountByShiftOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType, Time shiftTime,String requestType);
		public List<EFmFmAssignRoutePO> getAllTripsDetailsByShiftByVendorOTA(Date fromDate,Date toDate,String combinedBranchId,String tripType,Time shiftTime,int vendorId,String requestType);
		public List<EFmFmAssignRoutePO> getAllTripsDetailsByVendorWiseOnlyOTA(Date fromDate, Date toDate, String combinedBranchId,String tripType, int vendorId,String requestType);
		public long getAllEmployeesCountByShiftOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType, Time shiftTime,String requestType);
		public long getAllEmployeesCountByShiftByVendorIdOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType,Time shiftTime, int vendorId,String requestType);
		public long getNoShowEmployeesCountByShiftOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType, Time shiftTime,String requestType);
		public long getNoShowEmployeesCountByShiftByVendorOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType,Time shiftTime, int vendorId,String requestType);
		public long getPickedUpEmployeesCountByShiftOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType, Time shiftTime,String requestType);
		public long getPickedUpEmployeesCountByShiftByVendorOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType,Time shiftTime, int vendorId,String requestType);
		public List<EFmFmAssignRoutePO> getAllTripsDetailsByShiftOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType,Time shiftTime,String requestType);
		public List<EFmFmEmployeeTripDetailPO> getNoShowEmpCountByDateOTAView(Date fromDate, Date toDate, String combinedBranchId,
				String tripType, String requestType);
		public List<EFmFmEmployeeTripDetailPO> getNoShowEmployeesCountByShiftByVendorOTAView(Date fromDate, Date toDate,String combinedBranchId, String tripType, Time shiftTime, int vendorId, String requestType);
		/*
		 * Trip sheet
		 */
		public List<EFmFmAssignRoutePO> getAllTripByDateEmporGuest(Date fromDate, Date toDate, String combinedBranchId,String requestType);
		public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByDateWiseNoShow(Date fromDate, Date toDate, String combinedBranchId,
				String tripType, String requestType);
		public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByIdAndNameNoShow(Date fromDate, Date toDate, String combinedBranchId,
				String tripType, Time shiftTime, String requestType);
		public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByEmployeeIdNoShow(Date fromDate, Date toDate, String combinedBranchId,
				String tripType, Time shiftTime, String employeeId, String requestType);
		public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByEmployeeIdDateWiseNoShow(Date fromDate, Date toDate,
				int branchId, String tripType, String employeeId, String requestType);
		public List<EFmFmEmployeeTripDetailPO> getNoShowEmployeesCountByDateNoShowView(Date fromDate, Date toDate, String combinedBranchId,
				String tripType, String requestType);
		public List<EFmFmEmployeeTripDetailPO> getPickedUpEmployeesCountByDateView(Date fromDate, Date toDate, String combinedBranchId,
				String tripType, String requestType);
		public List<EFmFmAssignRoutePO> getAllTripsCountByDateNoShowView(Date fromDate, Date toDate, String combinedBranchId, String tripType,
				String requestType);
		public long getPickedUpOrDroppedEmployeesCountByDateSeatUtil(Date fromDate, Date toDate, String combinedBranchId, String tripType,
				String requestType);
		public List<EFmFmEmployeeTripDetailPO> getPickedUpOrDroppedEmployeesCountByDateSeatview(Date fromDate, Date toDate,
				String combinedBranchId, String tripType, String requestType);
		public List<EFmFmEmployeeTripDetailPO> getPickedUpEmployeesCountByShiftSeatUtilView(Date fromDate, Date toDate,
				String combinedBranchId, String tripType, Time shiftTime, String requestType);
		public List<EFmFmAssignRoutePO> getAllTripsCountByShiftSeatView(Date fromDate, Date toDate, String combinedBranchId, String tripType,
				Time shiftTime, String requestType);
		public List<Time> getAllTripsByAllShiftsOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType, String requestType);
		public List<Time> getAllTripsByAllShiftsForVendorOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType,
				int vendorId, String requestType);
		public List<Time> getAllTripsByShiftByVendorIdOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType, int vendorId,
				Time shiftTime, String requestType);
		public List<Date> getAllTripsByByVendorIdOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType, int vendorId,
				String requestType);
		public List<Time> getAllTripsByShiftOTA(Date fromDate, Date toDate, String combinedBranchId, String tripType, Time shiftTime,
				String requestType);
		public List<EFmFmAssignRoutePO> getAllRoutesOfParticularZoneWithOutDummyVehicles(EFmFmAssignRoutePO assignRoutePO);
		public List<EFmFmAssignRoutePO> getEmployeeCountByTrips(int assignRouteId, int branchId);
		public List<EFmFmEmployeeTripDetailPO> getNoShowEmployeesCountByShiftOTAView(Date fromDate, Date toDate, String combinedBranchId,
				String tripType, Time shiftTime, String requestType);
		public List<EFmFmAssignRoutePO> getCheckInTripAllocationCheckAfterTripCompletion(int checkInId, int branchId);
		public List<EFmFmAssignRoutePO> getAllLiveTripsByTripypeDateAndShiftTime(String branchId, String tripType, Time ShiftTime);
		public List<EFmFmLiveRoutTravelledPO> getRouteLastEtaAndDistanceFromAssignRouteId(int branchId, int assignRouteId);
		public List<EFmFmEmployeeTripDetailPO> getNoShowEmployeesCountByVendorWiseOTAView(Date fromDate, Date toDate, String combinedBranchId,
				String tripType, int vendorId, String requestType);
		public List<EFmFmLiveRoutTravelledPO> getRouteDetailsFromAssignRouteId(int branchId, int assignRouteId);
		public List<EFmFmLiveRoutTravelledPO> getRouteLastEtaAndDistanceFromAssignRouteIdForCabLastLocation(int branchId,
				int assignRouteId);
		public EFmFmLiveRoutTravelledPO getRouteLastEntryFromAssignRouteId(int branchId, int assignRouteId);
		public List<EFmFmLiveRoutTravelledPO> getCompletedRouteDataFromAssignRouteId(int branchId, int assignRouteId);
		public List<EFmFmAssignRoutePO> getParticularDateShiftTrips(Date fromDate, Date toDate, String tripType, String ShifTime,
				int branchId);
		public List<EFmFmAssignRoutePO> getAllTripByDateForGuest(Date fromDate, Date toDate, int branchId, String requestType);
		public List<EFmFmAssignRoutePO> getAllRoutesBasedOnTripTypeAndShiftTimeAdvanceSearch(EFmFmAssignRoutePO assignRoutePO);
		public List<EFmFmAssignRoutePO> getAllBucketClosedRoutesOnAdvanceSearch(EFmFmAssignRoutePO assignRoutePO);
		public List<EFmFmAssignRoutePO> getAllStartedRoutesOnAdvanceSearch(EFmFmAssignRoutePO assignRoutePO);
		public List<EFmFmAssignRoutePO> getAllOpenBucketRoutesOnAdvanceSearch(EFmFmAssignRoutePO assignRoutePO);
		public List<EFmFmAssignRoutePO> getAllBucketClosedAndNotStartedRoutesAdvanceSearch(EFmFmAssignRoutePO assignRoutePO);
		public List<EFmFmAssignRoutePO> getAllRoutesForPrintForParticularShift(Date fromDate, Date toDate, String tripType,
				String ShifTime, String branchId);
		public List<EFmFmAssignRoutePO> getAllGuestDynamicDetails(Date fromDate, Date toDate, String combinedBranchId);
		public List<Date> getAllTripsByDistinctDatesByVendor(Date fromDate, Date toDate, String combinedBranchId, String vendorId);
		public List<EFmFmAssignRoutePO> getAllTripByDateByVendor(Date fromDate, Date toDate, String combinedBranchId, String vendorId);
		public List<EFmFmAssignRoutePO> getAllTripsByDatesAndDriverIdByVendor(Date fromDate, Date toDate, String combinedBranchId,
				int driverId, String vendorId);
		public  List<EFmFmAssignRoutePO> getAllEmployeeDynamicDetails(Date fromDate, Date toDate, String combinedBranchId);
		public List<EFmFmAssignRoutePO> getAllTripByShiftTimeAndTripType(Date fromDate, Date toDate, String combinedBranchId, String tripType,
				String shiftTime);
		public List<EFmFmAssignRoutePO> getAllLiveTripsByShiftTime(int branchId, String tripType, Time ShiftTime,String createdDate);
		public List<EFmFmAssignRoutePO> getAllRoutesOfParticularZoneWithOrOutDummyVehicles(EFmFmAssignRoutePO assignRoutePO);
		public List<EFmFmAssignRoutePO> getAssignRouteDetailsByDate(EFmFmAssignRoutePO assignRoutePO);
		public List<EFmFmAssignRoutePO> getAllTripDetailsByVehicleId(Date fromDate, Date toDate, String combinedBranchId, int vehicleId);
		public List<EFmFmAssignRoutePO> getAllRoutesBasedOnTripTypeShiftTimeAndDate(EFmFmAssignRoutePO assignRoutePO);
		public List<EFmFmActualRoutTravelledPO> getRouteLattiLongiFromAssignRouteId(int assignRouteId);
		public List<EFmFmAssignRoutePO> getAllRoutesCountInsideZone(EFmFmAssignRoutePO assignRoutePO);
		public List<EFmFmAssignRoutePO> getAllActiveTripsAfterRouteClose(String tripType);
		public List<EFmFmAssignRoutePO> getLastTripFromCheckInIdAndTripType(int checkInId, String tripType);
		public List<EFmFmAssignRoutePO> getBackToBackTripDetailFromb2bId(int backTwoBackRouteId, String tripType,String branchId);
		public List<EFmFmAssignRoutePO> getBackToBackTripDetailFromTripTypeANdShiftTime(String tripType, Time shiftTime,String branchId);
		public List<EFmFmAssignRoutePO> getParticularRouteDetailFromAssignRouteId(int assignRouteId);
		public List<EFmFmAssignRoutePO> getAllBackToRoutesForParticularShift(EFmFmAssignRoutePO assignRoutePO);
		public List<EFmFmAssignRoutePO> getAllStartedTripsFromAssignRoute(String branchId);
		public List<EFmFmAssignRoutePO> getOpenRouteDetailFromAssignRouteId(Date fromDate, Date toDate, String tripType,
				String ShifTime, String branchId);
		public List<EFmFmAssignRoutePO> getAllOnRoadVehicleByVendors(int vendorId, int branchId);
		public List<EFmFmAssignRoutePO> getAllZoneByVendor(int vendorId, int branchId);
		public List<EFmFmAssignRoutePO> getAllRoutesOfParticularZoneByVendor(EFmFmAssignRoutePO assignRoutePO);
		public List<EFmFmAssignRoutePO> getListOfRoutesByVendor(EFmFmAssignRoutePO assignRoutePO);
		public void updateAssignVendorToRoute(int assignRouteId, String assignedVendorName);
		public List<EFmFmAssignRoutePO> getAllRoutesAllocatedToVendor(String assignedVendorName);
			/*
		 * CostBased Report
		 */
		public List<EFmFmAssignRoutePO> getListOfProjectIdsByDate(Date fromDate, Date toDate, String combinedBranchId);
		public List<EFmFmAssignRoutePO> getListOfDatesByProjectIds(Date fromDate, Date toDate, int branchId, int projectId);
		public List<EFmFmAssignRoutePO> getListOfRoutesByProjectIds(Date assignDate, int branchId, int projectId);
		public List<EFmFmEmployeeTripDetailPO> getListOfEmployeesByProjectIds(int  projectId, int assignRouteId);
		public List<EFmFmClientProjectDetailsPO> getProjectDetails(int projectId);
			public List<EFmFmEmployeeTripDetailPO> getAllFemaleEmployeesTravelledDetailByDate(Date fromDate, Date toDate);
			public List<EFmFmAssignRoutePO> getListOfTripsByProjectIdWithDate(Date fromDate, Date toDate, String combinedBranchId,
					int projectId);
			public List<EFmFmAssignRoutePO> getTravelledEmployeeCountByTrips(int assignRouteId, String combinedBranchId);
        public List<EFmFmAssignRoutePO> getAllNotDeliveredRoutesOnTripTypeAndShiftTime(EFmFmAssignRoutePO assignRoutePO);
		public List<EFmFmAssignRoutePO> getAllDeliveredRoutesOnTripTypeAndShiftTime(EFmFmAssignRoutePO assignRoutePO);
		List<Date> getAllTripsByShiftByVendor(Date fromDate, Date toDate, int branchId, String tripType, Time shiftTime,
				int vendorId);
		
}
