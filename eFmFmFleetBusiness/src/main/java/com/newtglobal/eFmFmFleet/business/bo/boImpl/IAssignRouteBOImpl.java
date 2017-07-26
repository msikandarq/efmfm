package com.newtglobal.eFmFmFleet.business.bo.boImpl;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtglobal.eFmFmFleet.business.bo.IAssignRouteBO;
import com.newtglobal.eFmFmFleet.business.dao.IAssignRouteDAO;
import com.newtglobal.eFmFmFleet.model.EFmCheckInVehicleTrackingPO;
import com.newtglobal.eFmFmFleet.model.EFmFmActualRoutTravelledPO;
import com.newtglobal.eFmFmFleet.model.EFmFmAssignRoutePO;
import com.newtglobal.eFmFmFleet.model.EFmFmClientProjectDetailsPO;
import com.newtglobal.eFmFmFleet.model.EFmFmEmployeeTripDetailPO;
import com.newtglobal.eFmFmFleet.model.EFmFmLiveRoutTravelledPO;

@Service("IAssignRouteBO")
public class IAssignRouteBOImpl implements IAssignRouteBO {

    @Autowired
    IAssignRouteDAO iAssignRouteDAO;

    public void setIUserMasterDAO(IAssignRouteDAO iAssignRouteDAO) {
        this.iAssignRouteDAO = iAssignRouteDAO;
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTodaysTrips(EFmFmAssignRoutePO assignRoutePO) {
        return iAssignRouteDAO.getAllTodaysTrips(assignRoutePO);
    }

    @Override
    public List<EFmFmAssignRoutePO> closeParticularTrips(EFmFmAssignRoutePO assignRoutePO) {
        return iAssignRouteDAO.closeParticularTrips(assignRoutePO);
    }

    @Override
    public void update(EFmFmAssignRoutePO eFmFmAssignRoutePO) {
        iAssignRouteDAO.update(eFmFmAssignRoutePO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllLiveTrips(EFmFmAssignRoutePO assignRoutePO) {
        return iAssignRouteDAO.getAllLiveTrips(assignRoutePO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllRoutesOfParticularZone(EFmFmAssignRoutePO assignRoutePO) {
        return iAssignRouteDAO.getAllRoutesOfParticularZone(assignRoutePO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllOnlyAssignedTrips(EFmFmAssignRoutePO assignRoutePO) {
        return iAssignRouteDAO.getAllOnlyAssignedTrips(assignRoutePO);
    }

    @Override
    public void save(EFmFmActualRoutTravelledPO actualRoutTravelledPO) {
        iAssignRouteDAO.save(actualRoutTravelledPO);
    }

    @Override
    public void update(EFmFmActualRoutTravelledPO actualRoutTravelledPO) {
        iAssignRouteDAO.update(actualRoutTravelledPO);
    }
    
    
    @Override
    public void save(EFmFmLiveRoutTravelledPO eFmFmLiveRoutTravelledPO) {
        iAssignRouteDAO.save(eFmFmLiveRoutTravelledPO);
    }

    @Override
    public void update(EFmFmLiveRoutTravelledPO eFmFmLiveRoutTravelledPO) {
        iAssignRouteDAO.update(eFmFmLiveRoutTravelledPO);
    }
    

    @Override
    public List<EFmFmActualRoutTravelledPO> getEtaAndDistanceFromAssignRouteId(
            EFmFmActualRoutTravelledPO actualRouteTravelledPO) {
        return iAssignRouteDAO.getEtaAndDistanceFromAssignRouteId(actualRouteTravelledPO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTodaysCompletedTrips(EFmFmAssignRoutePO assignRoutePO) {
        return iAssignRouteDAO.getAllTodaysCompletedTrips(assignRoutePO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getTripCountByDate(Date fromDate, Date toDate, String tripType, int clientId) {
        return iAssignRouteDAO.getTripCountByDate(fromDate, toDate, tripType, clientId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTripDetails(EFmFmAssignRoutePO assignRoutePO) {
        return iAssignRouteDAO.getAllTripDetails(assignRoutePO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTripByDate(Date fromDate, Date toDate, String clientId) {

        return iAssignRouteDAO.getAllTripByDate(fromDate, toDate, clientId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllActiveTrips(EFmFmAssignRoutePO assignRoutePO) {

        return iAssignRouteDAO.getAllActiveTrips(assignRoutePO);
    }

    @Override
    public void deleteParticularAssignRoute(int assignRouteId) {

        iAssignRouteDAO.deleteParticularAssignRoute(assignRouteId);
    }

    @Override
    public void deleteParticularActualTravelled(int travelId) {

        iAssignRouteDAO.deleteParticularActualTravelled(travelId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllRoutesOfParticularBranch(EFmFmAssignRoutePO assignRoutePO) {

        return iAssignRouteDAO.getAllRoutesOfParticularBranch(assignRoutePO);
    }

    @Override
    public void save(EFmFmAssignRoutePO eFmFmAssignRoutePO) {

        iAssignRouteDAO.save(eFmFmAssignRoutePO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllRoutesBasedOnTripTypeAndShiftTime(EFmFmAssignRoutePO assignRoutePO) {

        return iAssignRouteDAO.getAllRoutesBasedOnTripTypeAndShiftTime(assignRoutePO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllRoutesInsideZone(int branchId, int zoneId) {

        return iAssignRouteDAO.getAllRoutesInsideZone(branchId, zoneId);
    }

    @Override
    public long getPickupVehiclesOnRoadCounter(int branchId) {

        return iAssignRouteDAO.getPickupVehiclesOnRoadCounter(branchId);
    }

    @Override
    public long getDropVehiclesOnRoadCounter(int branchId) {

        return iAssignRouteDAO.getDropVehiclesOnRoadCounter(branchId);
    }

    @Override
    public long getAllVehiclesOnRoadCounter(String branchId) {

        return iAssignRouteDAO.getAllVehiclesOnRoadCounter(branchId);
    }

    @Override
    public List<EFmFmActualRoutTravelledPO> getCabLocationFromAssignRouteId(
            EFmFmActualRoutTravelledPO actualRouteTravelledPO) {
        return iAssignRouteDAO.getCabLocationFromAssignRouteId(actualRouteTravelledPO);
    }

    @Override
    public List<EFmFmActualRoutTravelledPO> getLastEtaFromAssignRouteId(
            EFmFmActualRoutTravelledPO actualRouteTravelledPO) {
        return iAssignRouteDAO.getLastEtaFromAssignRouteId(actualRouteTravelledPO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllBucketClosedRoutes(EFmFmAssignRoutePO assignRoutePO) {
        return iAssignRouteDAO.getAllBucketClosedRoutes(assignRoutePO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllStartedRoutes(EFmFmAssignRoutePO assignRoutePO) {
        return iAssignRouteDAO.getAllStartedRoutes(assignRoutePO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllClosedRoutes(String tripType, Time shiftTime, int branchId) {
        return iAssignRouteDAO.getAllClosedRoutes(tripType, shiftTime, branchId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllOpenBucketRoutes(EFmFmAssignRoutePO assignRoutePO) {

        return iAssignRouteDAO.getAllOpenBucketRoutes(assignRoutePO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTripsTravelledAndPlannedDistanceByDate(Date fromDate, Date toDate,
    		String branchId, int vendorId) {

        return iAssignRouteDAO.getAllTripsTravelledAndPlannedDistanceByDate(fromDate, toDate, branchId, vendorId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByDate(Date fromDate, Date toDate, String branchId) {

        return iAssignRouteDAO.getAllNoShowEmployeesByDate(fromDate, toDate, branchId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTripsTravelledAndPlannedDistanceByDateAndVehicle(Date fromDate, Date toDate,
            int branchId, int vehicleId) {

        return iAssignRouteDAO.getAllTripsTravelledAndPlannedDistanceByDateAndVehicle(fromDate, toDate, branchId,
                vehicleId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTripsTravelledAndPlannedDistanceByAllVendor(Date fromDate, Date toDate,
    		String branchId) {

        return iAssignRouteDAO.getAllTripsTravelledAndPlannedDistanceByAllVendor(fromDate, toDate, branchId);
    }

    @Override
    public String getVendorNameTravelledAndPlannedDistanceByAllVendor(Date fromDate, Date toDate, int branchId) {

        return iAssignRouteDAO.getVendorNameTravelledAndPlannedDistanceByAllVendor(fromDate, toDate, branchId);
    }

    @Override
    public List<Date> getAllTripsDistinctDates(Date fromDate, Date toDate, String branchId, String tripType) {

        return iAssignRouteDAO.getAllTripsDistinctDates(fromDate, toDate, branchId, tripType);
    }

    @Override
    public long getAllTripsCountByDate(Date fromDate, Date toDate, int branchId, String tripType) {

        return iAssignRouteDAO.getAllTripsCountByDate(fromDate, toDate, branchId, tripType);
    }

    @Override
    public long getAllDelayTripsCountByDate(Date fromDate, Date toDate, int branchId, String tripType) {

        return iAssignRouteDAO.getAllDelayTripsCountByDate(fromDate, toDate, branchId, tripType);
    }

    @Override
    public long getAllDelayTripsBeyondLoginTimeCountByDate(Date fromDate, Date toDate, int branchId, String tripType) {
        return iAssignRouteDAO.getAllDelayTripsBeyondLoginTimeCountByDate(fromDate, toDate, branchId, tripType);
    }

    @Override
    public long getAllEmployeesCountByDate(Date fromDate, Date toDate, int branchId, String tripType) {
        return iAssignRouteDAO.getAllEmployeesCountByDate(fromDate, toDate, branchId, tripType);
    }

    @Override
    public long getNoShowEmployeesCountByDate(Date fromDate, Date toDate, int branchId, String tripType) {
        return iAssignRouteDAO.getNoShowEmployeesCountByDate(fromDate, toDate, branchId, tripType);
    }

    @Override
    public long getPickedUpEmployeesCountByDate(Date fromDate, Date toDate, int branchId, String tripType) {
        return iAssignRouteDAO.getPickedUpEmployeesCountByDate(fromDate, toDate, branchId, tripType);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTripsByDate(Date fromDate, Date toDate, int branchId, String tripType) {
        return iAssignRouteDAO.getAllTripsByDate(fromDate, toDate, branchId, tripType);
    }

    @Override
    public List<Time> getAllTripsByShift(Date fromDate, Date toDate, int branchId, String tripType, Time shiftTime) {
        return iAssignRouteDAO.getAllTripsByShift(fromDate, toDate, branchId, tripType, shiftTime);
    }

    @Override
    public List<Date> getAllTripsByShiftByVendor(Date fromDate, Date toDate, int branchId, String tripType,
            Time shiftTime, int vendorId) {
        return iAssignRouteDAO.getAllTripsByShiftByVendor(fromDate, toDate, branchId, tripType, shiftTime, vendorId);
    }

    @Override
    public List<Date> getAllTripsByByVendorId(Date fromDate, Date toDate, int branchId, String tripType, int vendorId) {
        return iAssignRouteDAO.getAllTripsByByVendorId(fromDate, toDate, branchId, tripType, vendorId);
    }

    @Override
    public long getAllTripsCountByShift(Date fromDate, Date toDate, int branchId, String tripType, Time shiftWice) {
        return iAssignRouteDAO.getAllTripsCountByShift(fromDate, toDate, branchId, tripType, shiftWice);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTripsDetailsByShift(Date fromDate, Date toDate, String branchId, String tripType,
            Time shiftWice) {
        return iAssignRouteDAO.getAllTripsDetailsByShift(fromDate, toDate, branchId, tripType, shiftWice);
    }

    @Override
    public long getAllEmployeesCountByShift(Date fromDate, Date toDate, int branchId, String tripType, Time shiftWice) {
        return iAssignRouteDAO.getAllEmployeesCountByShift(fromDate, toDate, branchId, tripType, shiftWice);
    }

    @Override
    public long getNoShowEmployeesCountByShift(Date fromDate, Date toDate, int branchId, String tripType,
            Time shiftWice) {
        return iAssignRouteDAO.getNoShowEmployeesCountByShift(fromDate, toDate, branchId, tripType, shiftWice);
    }

    @Override
    public long getPickedUpEmployeesCountByShift(Date fromDate, Date toDate, int branchId, String tripType,
            Time shiftWice) {
        return iAssignRouteDAO.getPickedUpEmployeesCountByShift(fromDate, toDate, branchId, tripType, shiftWice);
    }

    @Override
    public List<Time> getAllTripsByShiftByVendorId(Date fromDate, Date toDate, int branchId, String tripType,
            int vendorId, Time shiftTime) {
        return iAssignRouteDAO.getAllTripsByShiftByVendorId(fromDate, toDate, branchId, tripType, vendorId, shiftTime);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByIdAndName(Date fromDate, Date toDate, int branchId,
            String tripType, Time shiftTime) {
        return iAssignRouteDAO.getAllNoShowEmployeesByIdAndName(fromDate, toDate, branchId, tripType, shiftTime);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByEmployeeId(Date fromDate, Date toDate, int branchId,
            String tripType, Time shiftTime, String employeeId) {
        return iAssignRouteDAO.getAllNoShowEmployeesByEmployeeId(fromDate, toDate, branchId, tripType, shiftTime,
                employeeId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByEmployeeIdDateWise(Date fromDate, Date toDate,
    		String branchId, String tripType, String employeeId) {
        return iAssignRouteDAO.getAllNoShowEmployeesByEmployeeIdDateWise(fromDate, toDate, branchId, tripType,
                employeeId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTripsDetailsByShiftByVendor(Date fromDate, Date toDate, int branchId,
            String tripType, Time shiftWice, int vendorId) {
        return iAssignRouteDAO.getAllTripsDetailsByShiftByVendor(fromDate, toDate, branchId, tripType, shiftWice,
                vendorId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTripsDetailsByVendorWiseOnly(Date fromDate, Date toDate, int branchId,
            String tripType, int vendorId) {
        return iAssignRouteDAO.getAllTripsDetailsByVendorWiseOnly(fromDate, toDate, branchId, tripType, vendorId);
    }

    @Override
    public List<Time> getAllTripsByAllShifts(Date fromDate, Date toDate, int branchId, String tripType) {
        return iAssignRouteDAO.getAllTripsByAllShifts(fromDate, toDate, branchId, tripType);
    }

    @Override
    public List<Time> getAllTripsByAllShiftsForVendor(Date fromDate, Date toDate, int branchId, String tripType,
            int vendorId) {
        return iAssignRouteDAO.getAllTripsByAllShiftsForVendor(fromDate, toDate, branchId, tripType, vendorId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByDateWise(Date fromDate, Date toDate, int branchId,
            String tripType) {
        return iAssignRouteDAO.getAllNoShowEmployeesByDateWise(fromDate, toDate, branchId, tripType);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTripsVehicleDetailsByVehicleNumber(Date fromDate, Date toDate, String branchId,
            String vehicleNumber) {
        return iAssignRouteDAO.getAllTripsVehicleDetailsByVehicleNumber(fromDate, toDate, branchId, vehicleNumber);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTripsVehicleKMDetailsByShiftTime(Date fromDate, Date toDate, String branchId,
            Time shiftTime) {
        return iAssignRouteDAO.getAllTripsVehicleKMDetailsByShiftTime(fromDate, toDate, branchId, shiftTime);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllMessageEmployeesMessageDetailsByDate(Date fromDate, Date toDate,
    		String branchId) {
        return iAssignRouteDAO.getAllMessageEmployeesMessageDetailsByDate(fromDate, toDate, branchId);
    }

    @Override
    public long getUsedFleetByByVendorId(Date fromDate, Date toDate, int branchId, String tripType, int vendorId) {
        return iAssignRouteDAO.getUsedFleetByByVendorId(fromDate, toDate, branchId, tripType, vendorId);
    }

    @Override
    public long getNoShowEmployeesCountByDateByVendor(Date fromDate, Date toDate, int branchId, String tripType,
            int vendorId) {
        return iAssignRouteDAO.getNoShowEmployeesCountByDateByVendor(fromDate, toDate, branchId, tripType, vendorId);
    }

    @Override
    public long getPickedUpEmployeesCountByDateByVendor(Date fromDate, Date toDate, int branchId, String tripType,
            int vendorId) {
        return iAssignRouteDAO.getPickedUpEmployeesCountByDateByVendor(fromDate, toDate, branchId, tripType, vendorId);
    }

    @Override
    public long getPickedUpEmployeesCountByShiftByVendor(Date fromDate, Date toDate, int branchId, String tripType,
            Time shiftTime, int vendorId) {
        return iAssignRouteDAO.getPickedUpEmployeesCountByShiftByVendor(fromDate, toDate, branchId, tripType, shiftTime,
                vendorId);
    }

    @Override
    public long getNoShowEmployeesCountByShiftByVendor(Date fromDate, Date toDate, int branchId, String tripType,
            Time shiftTime, int vendorId) {
        return iAssignRouteDAO.getNoShowEmployeesCountByShiftByVendor(fromDate, toDate, branchId, tripType, shiftTime,
                vendorId);
    }

    @Override
    public long getUsedFleetByByVendorIdByShift(Date fromDate, Date toDate, int branchId, String tripType, int vendorId,
            Time shiftTime) {
        return iAssignRouteDAO.getUsedFleetByByVendorIdByShift(fromDate, toDate, branchId, tripType, vendorId,
                shiftTime);
    }

    @Override
    public long getAllEmployeesCountByDateByVendorId(Date fromDate, Date toDate, int branchId, String tripType,
            int vendorId) {
        return iAssignRouteDAO.getAllEmployeesCountByDateByVendorId(fromDate, toDate, branchId, tripType, vendorId);
    }

    @Override
    public long getAllEmployeesCountByShiftByVendorId(Date fromDate, Date toDate, int branchId, String tripType,
            Time shiftTime, int vendorId) {
        return iAssignRouteDAO.getAllEmployeesCountByShiftByVendorId(fromDate, toDate, branchId, tripType, shiftTime,
                vendorId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getTodayTripByShift(Date fromDate, Date toDate, String tripType, String ShifTime,
            int branchId) {
        return iAssignRouteDAO.getTodayTripByShift(fromDate, toDate, tripType, ShifTime, branchId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getExportTodayTrips(Date fromDate, Date toDate, String tripType, String ShifTime,
            String branchId) {
        return iAssignRouteDAO.getExportTodayTrips(fromDate, toDate, tripType, ShifTime, branchId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllEscortRequiredTripsByDate(Date fromDate, Date toDate, String branchId) {
        return iAssignRouteDAO.getAllEscortRequiredTripsByDate(fromDate, toDate, branchId);
    }

    @Override
    public List<Date> getAllTripsByDistinctDates(Date fromDate, Date toDate, String branchId) {
        return iAssignRouteDAO.getAllTripsByDistinctDates(fromDate, toDate, branchId);
    }

    @Override
    public long getPickedUpOrDroppedEmployeesCountByDate(Date fromDate, Date toDate, int branchId, String tripType) {
        return iAssignRouteDAO.getPickedUpOrDroppedEmployeesCountByDate(fromDate, toDate, branchId, tripType);
    }

    @Override
    public List<ArrayList> getAllTripsByDistinctDatesAndDeriverId(Date fromDate, Date toDate, int branchId) {
        return iAssignRouteDAO.getAllTripsByDistinctDatesAndDeriverId(fromDate, toDate, branchId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTripByDateByDeriverId(Date fromDate, Date toDate, int branchId,
            int driverId) {
        return iAssignRouteDAO.getAllTripByDateByDeriverId(fromDate, toDate, branchId, driverId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTripsByDatesAndDriverId(Date fromDate, Date toDate, String branchId,
            int driverId) {
        return iAssignRouteDAO.getAllTripsByDatesAndDriverId(fromDate, toDate, branchId, driverId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllBucketClosedAndNotStartedRoutes(EFmFmAssignRoutePO assignRoutePO) {
        return iAssignRouteDAO.getAllBucketClosedAndNotStartedRoutes(assignRoutePO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllTripForTravelTimeReports(Date fromDate, Date toDate, String tripType,
    		String branchId) {
        return iAssignRouteDAO.getAllTripForTravelTimeReports(fromDate, toDate, tripType, branchId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllVehicleDistance(Date fromDate, Date toDate, int branchId, int vehicleId) {
        return iAssignRouteDAO.getAllVehicleDistance(fromDate, toDate, branchId, vehicleId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getParticuarEmployeesTravelledDetailByEmployeeId(Date fromDate, Date toDate,
            int branchId, String tripType, String employeeId) {
        
        return iAssignRouteDAO.getParticuarEmployeesTravelledDetailByEmployeeId(fromDate, toDate, branchId, tripType, employeeId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getParticuarEmployeesTravelledDetailByEmployeeIdAndBranchId(Date fromDate,
            Date toDate, String branchId, String employeeId) {
        
        return iAssignRouteDAO.getParticuarEmployeesTravelledDetailByEmployeeIdAndBranchId(fromDate, toDate, branchId, employeeId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getAllEdiTripByBranchId(String branchId) {
        
        return iAssignRouteDAO.getAllEdiTripByBranchId(branchId);
    }

    @Override
    public List<EFmCheckInVehicleTrackingPO> vehicleTrackingAfterCheckIn(String branhId, int checkInId) {
        
        return iAssignRouteDAO.vehicleTrackingAfterCheckIn(branhId, checkInId);
    }

	@Override
	public List<EFmFmAssignRoutePO> getLastCompletedTripByDriver(int checkInId, int branchId) {
		return iAssignRouteDAO.getLastCompletedTripByDriver(checkInId, branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllTodaysTripsWithOutDummyVehicle(EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAllTodaysTripsWithOutDummyVehicle(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getDuplicateTripAllocationCheck(int checkInId, String branchId) {
		return iAssignRouteDAO.getDuplicateTripAllocationCheck(checkInId, branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getTripAllocatedRoute(int checkInId, int branchId) {
		return iAssignRouteDAO.getTripAllocatedRoute(checkInId, branchId);
	}
	
	@Override
	public List<Date> getAllTripsDistinctDatesOTA(Date fromDate, Date toDate, String branchId, String tripType,String requestType) {		
		return iAssignRouteDAO.getAllTripsDistinctDatesOTA(fromDate, toDate, branchId, tripType,requestType);
	}

	@Override
	public long getUsedFleetByByVendorIdOTA(Date fromDate, Date toDate, String branchId, String tripType, int vendorId,String requestType) {		
		return iAssignRouteDAO.getUsedFleetByByVendorIdOTA(fromDate, toDate, branchId, tripType, vendorId,requestType);
	}

	@Override
	public long getUsedFleetByByVendorIdByShiftOTA(Date fromDate, Date toDate, String branchId, String tripType,
			int vendorId, Time shiftTime,String requestType) {		
		return iAssignRouteDAO.getUsedFleetByByVendorIdByShiftOTA(fromDate, toDate, branchId, tripType, vendorId, shiftTime,requestType);
	}
	@Override
	public long getAllTripsCountByDateOTA(Date fromDate, Date toDate, String branchId, String tripType,String requestType) {
		return iAssignRouteDAO.getAllTripsCountByDateOTA(fromDate, toDate, branchId, tripType,requestType);
	}
	@Override
	public long getAllDelayTripsCountByDateOTA(Date fromDate, Date toDate, int branchId, String tripType,String requestType) {
		return iAssignRouteDAO.getAllDelayTripsCountByDateOTA(fromDate, toDate, branchId, tripType,requestType);
	}
	@Override
	public long getAllDelayTripsBeyondLoginTimeCountByDateOTA(Date fromDate, Date toDate, int branchId,
			String tripType,String requestType) {
		return iAssignRouteDAO.getAllDelayTripsBeyondLoginTimeCountByDateOTA(fromDate, toDate, branchId, tripType,requestType);
	}
	@Override
	public long getAllEmployeesCountByDateOTA(Date fromDate, Date toDate, String branchId, String tripType,String requestType) {
		return iAssignRouteDAO.getAllEmployeesCountByDateOTA(fromDate, toDate, branchId, tripType,requestType);
	}
	@Override
	public long getAllEmployeesCountByDateByVendorIdOTA(Date fromDate, Date toDate, String branchId, String tripType,
			int vendorId,String requestType) {
		return iAssignRouteDAO.getAllEmployeesCountByDateByVendorIdOTA(fromDate, toDate, branchId, tripType, vendorId,requestType);
	}

	@Override
	public long getNoShowEmployeesCountByDateOTA(Date fromDate, Date toDate, String branchId, String tripType,String requestType) {
		return iAssignRouteDAO.getNoShowEmployeesCountByDateOTA(fromDate, toDate, branchId, tripType,requestType);
	}

	@Override
	public long getNoShowEmployeesCountByDateByVendorOTA(Date fromDate, Date toDate, String branchId, String tripType,
			int vendorId,String requestType) {
		return iAssignRouteDAO.getNoShowEmployeesCountByDateByVendorOTA(fromDate, toDate, branchId, tripType, vendorId,requestType);
	}

	@Override
	public long getPickedUpEmployeesCountByDateOTA(Date fromDate, Date toDate, String branchId, String tripType,String requestType) {
		return iAssignRouteDAO.getPickedUpEmployeesCountByDateOTA(fromDate, toDate, branchId, tripType,requestType);
	}

	@Override
	public long getPickedUpEmployeesCountByDateByVendorOTA(Date fromDate, Date toDate, String branchId, String tripType,
			int vendorId,String requestType) {
		return iAssignRouteDAO.getPickedUpEmployeesCountByDateByVendorOTA(fromDate, toDate, branchId, tripType, vendorId,requestType);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllTripsByDateOTA(Date fromDate, Date toDate, String branchId, String tripType,String requestType) {	
		return iAssignRouteDAO.getAllTripsByDateOTA(fromDate, toDate, branchId, tripType,requestType);
	}

	@Override
	public long getAllTripsCountByShiftOTA(Date fromDate, Date toDate, String branchId, String tripType, Time shiftTime,String requestType) {
		return iAssignRouteDAO.getAllTripsCountByShiftOTA(fromDate, toDate, branchId, tripType, shiftTime,requestType);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllTripsDetailsByShiftByVendorOTA(Date fromDate, Date toDate, String branchId,
			String tripType, Time shiftTime, int vendorId,String requestType) {
		return iAssignRouteDAO.getAllTripsDetailsByShiftByVendorOTA(fromDate, toDate, branchId, tripType, shiftTime, vendorId,requestType);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllTripsDetailsByVendorWiseOnlyOTA(Date fromDate, Date toDate, String branchId,
			String tripType, int vendorId,String requestType) {
		return iAssignRouteDAO.getAllTripsDetailsByVendorWiseOnlyOTA(fromDate, toDate, branchId, tripType, vendorId,requestType);
	}

	@Override
	public long getAllEmployeesCountByShiftOTA(Date fromDate, Date toDate, String branchId, String tripType,
			Time shiftTime,String requestType) {
		return iAssignRouteDAO.getAllEmployeesCountByShiftOTA(fromDate, toDate, branchId, tripType, shiftTime,requestType);
	}

	@Override
	public long getAllEmployeesCountByShiftByVendorIdOTA(Date fromDate, Date toDate, String branchId, String tripType,
			Time shiftTime, int vendorId,String requestType) {
		return iAssignRouteDAO.getAllEmployeesCountByShiftByVendorIdOTA(fromDate, toDate, branchId, tripType, shiftTime, vendorId,requestType);
	}

	@Override
	public long getNoShowEmployeesCountByShiftOTA(Date fromDate, Date toDate, String branchId, String tripType,
			Time shiftTime,String requestType) {
		return iAssignRouteDAO.getNoShowEmployeesCountByShiftOTA(fromDate, toDate, branchId, tripType, shiftTime,requestType);
	}

	@Override
	public long getNoShowEmployeesCountByShiftByVendorOTA(Date fromDate, Date toDate, String branchId, String tripType,
			Time shiftTime, int vendorId,String requestType) {	
		return iAssignRouteDAO.getNoShowEmployeesCountByShiftByVendorOTA(fromDate, toDate, branchId, tripType, shiftTime, vendorId,requestType);
	}

	@Override
	public long getPickedUpEmployeesCountByShiftOTA(Date fromDate, Date toDate, String branchId, String tripType,
			Time shiftTime,String requestType) {	
		return iAssignRouteDAO.getPickedUpEmployeesCountByShiftOTA(fromDate, toDate, branchId, tripType, shiftTime,requestType);
	}

	@Override
	public long getPickedUpEmployeesCountByShiftByVendorOTA(Date fromDate, Date toDate, String branchId, String tripType,
			Time shiftTime, int vendorId,String requestType) {		
		return iAssignRouteDAO.getPickedUpEmployeesCountByShiftByVendorOTA(fromDate, toDate, branchId, tripType, shiftTime, vendorId,requestType);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllTripsDetailsByShiftOTA(Date fromDate, Date toDate, String branchId,
			String tripType, Time shiftTime,String requestType) {	
		return iAssignRouteDAO.getAllTripsDetailsByShiftOTA(fromDate, toDate, branchId, tripType, shiftTime,requestType);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getNoShowEmpCountByDateOTAView(Date fromDate, Date toDate, String branchId,
			String tripType, String requestType) {		
		return iAssignRouteDAO.getNoShowEmpCountByDateOTAView(fromDate, toDate, branchId, tripType, requestType);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getNoShowEmployeesCountByShiftByVendorOTAView(Date fromDate, Date toDate,
			String branchId, String tripType, Time shiftTime, int vendorId, String requestType) {	
		return iAssignRouteDAO.getNoShowEmployeesCountByShiftByVendorOTAView(fromDate, toDate, branchId, tripType, shiftTime, vendorId, requestType);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllTripByDateEmporGuest(Date fromDate, Date toDate, String branchId,String requestType) {
			return iAssignRouteDAO.getAllTripByDateEmporGuest(fromDate, toDate, branchId,requestType);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByDateWiseNoShow(Date fromDate, Date toDate, String branchId,
			String tripType, String requestType) {		
		return iAssignRouteDAO.getAllNoShowEmployeesByDateWiseNoShow(fromDate, toDate, branchId, tripType, requestType);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByIdAndNameNoShow(Date fromDate, Date toDate,
			String branchId, String tripType, Time shiftTime, String requestType) {		
		return iAssignRouteDAO.getAllNoShowEmployeesByIdAndNameNoShow(fromDate, toDate, branchId, tripType, shiftTime, requestType);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByEmployeeIdNoShow(Date fromDate, Date toDate,
			String branchId, String tripType, Time shiftTime, String employeeId, String requestType) {		
		return iAssignRouteDAO.getAllNoShowEmployeesByEmployeeIdNoShow(fromDate, toDate, branchId, tripType, shiftTime, employeeId, requestType);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getAllNoShowEmployeesByEmployeeIdDateWiseNoShow(Date fromDate, Date toDate,
			int branchId, String tripType, String employeeId, String requestType) {		
		return iAssignRouteDAO.getAllNoShowEmployeesByEmployeeIdDateWiseNoShow(fromDate, toDate, branchId, tripType, employeeId, requestType);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getNoShowEmployeesCountByDateNoShowView(Date fromDate, Date toDate,
			String branchId, String tripType, String requestType) {		
		return iAssignRouteDAO.getNoShowEmployeesCountByDateNoShowView(fromDate, toDate, branchId, tripType, requestType);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getPickedUpEmployeesCountByDateView(Date fromDate, Date toDate, String branchId,
			String tripType, String requestType) {		
		return iAssignRouteDAO.getPickedUpEmployeesCountByDateView(fromDate, toDate, branchId, tripType, requestType);
	}
	@Override
	public List<EFmFmAssignRoutePO> getAllTripsCountByDateNoShowView(Date fromDate, Date toDate, String branchId,
			String tripType, String requestType) {		
		return iAssignRouteDAO.getAllTripsCountByDateNoShowView(fromDate, toDate, branchId, tripType, requestType);
	}

	@Override
	public long getPickedUpOrDroppedEmployeesCountByDateSeatUtil(Date fromDate, Date toDate, String branchId,
			String tripType, String requestType) {		
		return iAssignRouteDAO.getPickedUpOrDroppedEmployeesCountByDateSeatUtil(fromDate, toDate, branchId, tripType, requestType);
	}
	@Override
	public List<EFmFmEmployeeTripDetailPO> getPickedUpOrDroppedEmployeesCountByDateSeatview(Date fromDate, Date toDate,
			String branchId, String tripType, String requestType) {		
		return iAssignRouteDAO.getPickedUpOrDroppedEmployeesCountByDateSeatview(fromDate, toDate, branchId, tripType, requestType);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getPickedUpEmployeesCountByShiftSeatUtilView(Date fromDate, Date toDate,
			String branchId, String tripType, Time shiftTime, String requestType) {
		return iAssignRouteDAO.getPickedUpEmployeesCountByShiftSeatUtilView(fromDate, toDate, branchId, tripType, shiftTime, requestType);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllTripsCountByShiftSeatView(Date fromDate, Date toDate, String branchId,
			String tripType, Time shiftTime, String requestType) {
		return iAssignRouteDAO.getAllTripsCountByShiftSeatView(fromDate, toDate, branchId, tripType, shiftTime, requestType);
	}

	@Override
	public List<Time> getAllTripsByAllShiftsOTA(Date fromDate, Date toDate, String branchId, String tripType,
			String requestType) {		
		return iAssignRouteDAO.getAllTripsByAllShiftsOTA(fromDate, toDate, branchId, tripType, requestType);
	}

	@Override
	public List<Time> getAllTripsByAllShiftsForVendorOTA(Date fromDate, Date toDate, String branchId, String tripType,
			int vendorId, String requestType) {	
		return iAssignRouteDAO.getAllTripsByAllShiftsForVendorOTA(fromDate, toDate, branchId, tripType, vendorId, requestType);
	}

	@Override
	public List<Time> getAllTripsByShiftByVendorIdOTA(Date fromDate, Date toDate, String branchId, String tripType,
			int vendorId, Time shiftTime, String requestType) {		
		return iAssignRouteDAO.getAllTripsByShiftByVendorIdOTA(fromDate, toDate, branchId, tripType, vendorId, shiftTime, requestType);
	}

	@Override
	public List<Date> getAllTripsByByVendorIdOTA(Date fromDate, Date toDate, String branchId, String tripType,
			int vendorId, String requestType) {		
		return iAssignRouteDAO.getAllTripsByByVendorIdOTA(fromDate, toDate, branchId, tripType, vendorId, requestType);
	}

	@Override
	public List<Time> getAllTripsByShiftOTA(Date fromDate, Date toDate, String branchId, String tripType, Time shiftTime,
			String requestType) {		
		return iAssignRouteDAO.getAllTripsByShiftOTA(fromDate, toDate, branchId, tripType, shiftTime, requestType);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllRoutesOfParticularZoneWithOutDummyVehicles(EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAllRoutesOfParticularZoneWithOutDummyVehicles(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getEmployeeCountByTrips(int assignRouteId, int branchId) {
		return iAssignRouteDAO.getEmployeeCountByTrips(assignRouteId, branchId);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getNoShowEmployeesCountByShiftOTAView(Date fromDate, Date toDate,
			String branchId, String tripType, Time shiftTime, String requestType) {
		return iAssignRouteDAO.getNoShowEmployeesCountByShiftOTAView(fromDate, toDate, branchId, tripType, shiftTime, requestType);
	}

	@Override
	public List<EFmFmAssignRoutePO> getCheckInTripAllocationCheckAfterTripCompletion(int checkInId, int branchId) {
		return iAssignRouteDAO.getCheckInTripAllocationCheckAfterTripCompletion(checkInId, branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllLiveTripsByTripypeDateAndShiftTime(String branchId, String tripType,
			Time ShiftTime) {
		return iAssignRouteDAO.getAllLiveTripsByTripypeDateAndShiftTime(branchId, tripType, ShiftTime);
	}

	@Override
	public List<EFmFmLiveRoutTravelledPO> getRouteLastEtaAndDistanceFromAssignRouteId(int branchId,
			int assignRouteId) {
		return iAssignRouteDAO.getRouteLastEtaAndDistanceFromAssignRouteId(branchId, assignRouteId);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getNoShowEmployeesCountByVendorWiseOTAView(Date fromDate, Date toDate,
			String branchId, String tripType, int vendorId, String requestType) {
		return iAssignRouteDAO.getNoShowEmployeesCountByVendorWiseOTAView(fromDate, toDate, branchId, tripType, vendorId, requestType);
	}

	@Override
	public List<EFmFmLiveRoutTravelledPO> getRouteDetailsFromAssignRouteId(int branchId, int assignRouteId) {
		return iAssignRouteDAO.getRouteDetailsFromAssignRouteId(branchId, assignRouteId);
	}

	@Override
	public List<EFmFmLiveRoutTravelledPO> getRouteLastEtaAndDistanceFromAssignRouteIdForCabLastLocation(int branchId,
			int assignRouteId) {
		return iAssignRouteDAO.getRouteLastEtaAndDistanceFromAssignRouteIdForCabLastLocation(branchId, assignRouteId);
	}

	@Override
	public EFmFmLiveRoutTravelledPO getRouteLastEntryFromAssignRouteId(int branchId, int assignRouteId) {
		return iAssignRouteDAO.getRouteLastEntryFromAssignRouteId(branchId, assignRouteId);
	}

	@Override
	public List<EFmFmLiveRoutTravelledPO> getCompletedRouteDataFromAssignRouteId(int branchId, int assignRouteId) {
		return iAssignRouteDAO.getCompletedRouteDataFromAssignRouteId(branchId, assignRouteId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getParticularDateShiftTrips(Date fromDate, Date toDate, String tripType,
			String ShifTime, int branchId) {
		return iAssignRouteDAO.getParticularDateShiftTrips(fromDate, toDate, tripType, ShifTime, branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllTripByDateForGuest(Date fromDate, Date toDate, int branchId,
			String requestType) {
		return iAssignRouteDAO.getAllTripByDateForGuest(fromDate, toDate, branchId, requestType);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllRoutesBasedOnTripTypeAndShiftTimeAdvanceSearch(
			EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAllRoutesBasedOnTripTypeAndShiftTimeAdvanceSearch(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllBucketClosedRoutesOnAdvanceSearch(EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAllBucketClosedRoutesOnAdvanceSearch(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllStartedRoutesOnAdvanceSearch(EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAllStartedRoutesOnAdvanceSearch(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllOpenBucketRoutesOnAdvanceSearch(EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAllOpenBucketRoutesOnAdvanceSearch(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllBucketClosedAndNotStartedRoutesAdvanceSearch(
			EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAllBucketClosedAndNotStartedRoutesAdvanceSearch(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllRoutesForPrintForParticularShift(Date fromDate, Date toDate, String tripType,
			String ShifTime, String branchId) {
		return iAssignRouteDAO.getAllRoutesForPrintForParticularShift(fromDate, toDate, tripType, ShifTime, branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllGuestDynamicDetails(Date fromDate, Date toDate, String branchId) {
		return iAssignRouteDAO.getAllGuestDynamicDetails(fromDate, toDate, branchId);
	}

	@Override
	public List<Date> getAllTripsByDistinctDatesByVendor(Date fromDate, Date toDate, String branchId, String vendorId) {
		return iAssignRouteDAO.getAllTripsByDistinctDatesByVendor(fromDate, toDate, branchId, vendorId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllTripByDateByVendor(Date fromDate, Date toDate, String branchId,
			String vendorId) {
		return iAssignRouteDAO.getAllTripByDateByVendor(fromDate, toDate, branchId, vendorId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllTripsByDatesAndDriverIdByVendor(Date fromDate, Date toDate, String branchId,
			int driverId, String vendorId) {
		return iAssignRouteDAO.getAllTripsByDatesAndDriverIdByVendor(fromDate, toDate, branchId, driverId, vendorId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllEmployeeDynamicDetails(Date fromDate, Date toDate, String branchId) {
		return iAssignRouteDAO.getAllEmployeeDynamicDetails(fromDate, toDate, branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllTripByShiftTimeAndTripType(Date fromDate, Date toDate, String branchId,
			String tripType, String shiftTime) {
		return iAssignRouteDAO.getAllTripByShiftTimeAndTripType(fromDate, toDate, branchId, tripType, shiftTime);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllLiveTripsByShiftTime(int branchId, String tripType, Time ShiftTime,String createdDate) {
		return iAssignRouteDAO.getAllLiveTripsByShiftTime(branchId, tripType, ShiftTime,createdDate);
				
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllRoutesOfParticularZoneWithOrOutDummyVehicles(
			EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAllRoutesOfParticularZoneWithOrOutDummyVehicles(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAssignRouteDetailsByDate(EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAssignRouteDetailsByDate(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllTripDetailsByVehicleId(Date fromDate, Date toDate, String branchId,
			int vehicleId) {
		return iAssignRouteDAO.getAllTripDetailsByVehicleId(fromDate, toDate, branchId, vehicleId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllRoutesBasedOnTripTypeShiftTimeAndDate(EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAllRoutesBasedOnTripTypeShiftTimeAndDate(assignRoutePO);
	}

	@Override
	public List<EFmFmActualRoutTravelledPO> getRouteLattiLongiFromAssignRouteId(int assignRouteId) {
		return iAssignRouteDAO.getRouteLattiLongiFromAssignRouteId(assignRouteId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllRoutesCountInsideZone(EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAllRoutesCountInsideZone(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllActiveTripsAfterRouteClose(String tripType) {
		return iAssignRouteDAO.getAllActiveTripsAfterRouteClose(tripType);
	}

	@Override
	public List<EFmFmAssignRoutePO> getLastTripFromCheckInIdAndTripType(int checkInId, String tripType) {
		return iAssignRouteDAO.getLastTripFromCheckInIdAndTripType(checkInId, tripType);
	}

	@Override
	public List<EFmFmAssignRoutePO> getBackToBackTripDetailFromb2bId(int backTwoBackRouteId, String tripType,String branchId) {
		return iAssignRouteDAO.getBackToBackTripDetailFromb2bId(backTwoBackRouteId, tripType,branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getBackToBackTripDetailFromTripTypeANdShiftTime(String tripType, Time shiftTime,String branchId) {
		return iAssignRouteDAO.getBackToBackTripDetailFromTripTypeANdShiftTime(tripType, shiftTime,branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getParticularRouteDetailFromAssignRouteId(int assignRouteId) {
		return iAssignRouteDAO.getParticularRouteDetailFromAssignRouteId(assignRouteId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllBackToRoutesForParticularShift(EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAllBackToRoutesForParticularShift(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllStartedTripsFromAssignRoute(String branchId) {
		return iAssignRouteDAO.getAllStartedTripsFromAssignRoute(branchId);
	}


	@Override
	public List<EFmFmAssignRoutePO> getOpenRouteDetailFromAssignRouteId(Date fromDate, Date toDate, String tripType,
			String ShifTime, String branchId) {
		return iAssignRouteDAO.getOpenRouteDetailFromAssignRouteId(fromDate, toDate, tripType, ShifTime, branchId);
	}
	
		@Override
	public List<EFmFmAssignRoutePO> getAllOnRoadVehicleByVendors(int vendorId, int branchId) {
		return iAssignRouteDAO.getAllOnRoadVehicleByVendors(vendorId, branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllZoneByVendor(int vendorId, int branchId) {
		return iAssignRouteDAO.getAllZoneByVendor(vendorId, branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllRoutesOfParticularZoneByVendor(EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAllRoutesOfParticularZoneByVendor(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getListOfRoutesByVendor(EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getListOfRoutesByVendor(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getListOfProjectIdsByDate(Date fromDate, Date toDate, String branchId) {
		// TODO Auto-generated method stub
		return iAssignRouteDAO.getListOfProjectIdsByDate(fromDate, toDate, branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getListOfDatesByProjectIds(Date fromDate, Date toDate, int branchId,
			int projectId) {
		// TODO Auto-generated method stub
		return iAssignRouteDAO.getListOfDatesByProjectIds(fromDate, toDate, branchId, projectId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getListOfRoutesByProjectIds(Date assignDate, int branchId, int projectId) {
		// TODO Auto-generated method stub
		return iAssignRouteDAO.getListOfRoutesByProjectIds(assignDate, branchId, projectId);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getListOfEmployeesByProjectIds(int projectId, int assignRouteId) {
		// TODO Auto-generated method stub
		return iAssignRouteDAO.getListOfEmployeesByProjectIds(projectId, assignRouteId);
	}

	@Override
	public List<EFmFmClientProjectDetailsPO> getProjectDetails(int projectId) {
		// TODO Auto-generated method stub
		return iAssignRouteDAO.getProjectDetails(projectId);
	}
	@Override
	public void updateAssignVendorToRoute(int assignRouteId, String assignedVendorName) {
        iAssignRouteDAO.updateAssignVendorToRoute(assignRouteId, assignedVendorName);

	}

	@Override
	public List<EFmFmAssignRoutePO> getAllRoutesAllocatedToVendor(String assignedVendorName) {
		return iAssignRouteDAO.getAllRoutesAllocatedToVendor(assignedVendorName);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getAllFemaleEmployeesTravelledDetailByDate(Date fromDate, Date toDate) {
		return iAssignRouteDAO.getAllFemaleEmployeesTravelledDetailByDate(fromDate, toDate);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllNotDeliveredRoutesOnTripTypeAndShiftTime(EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAllNotDeliveredRoutesOnTripTypeAndShiftTime(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getAllDeliveredRoutesOnTripTypeAndShiftTime(EFmFmAssignRoutePO assignRoutePO) {
		return iAssignRouteDAO.getAllDeliveredRoutesOnTripTypeAndShiftTime(assignRoutePO);
	}

	@Override
	public List<EFmFmAssignRoutePO> getListOfTripsByProjectIdWithDate(Date fromDate, Date toDate, String branchId,
			int projectId) {
		// TODO Auto-generated method stub
		return iAssignRouteDAO.getListOfTripsByProjectIdWithDate(fromDate, toDate, branchId, projectId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getTravelledEmployeeCountByTrips(int assignRouteId, String branchId) {
		// TODO Auto-generated method stub
		return iAssignRouteDAO.getTravelledEmployeeCountByTrips(assignRouteId, branchId);
	}

}
