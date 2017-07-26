package com.newtglobal.eFmFmFleet.business.bo.boImpl;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newtglobal.eFmFmFleet.business.bo.ICabRequestBO;
import com.newtglobal.eFmFmFleet.business.dao.ICabRequestDAO;
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
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleCheckInPO;
import com.newtglobal.eFmFmFleet.model.EFmFmVehicleMasterPO;

@Service("ICabRequestBO")
public class ICabRequestBOImpl implements ICabRequestBO {

    @Autowired
    ICabRequestDAO iCabRequestDAO;

    public void setiCabRequestDAO(ICabRequestDAO iCabRequestDAO) {
        this.iCabRequestDAO = iCabRequestDAO;
    }

    @Override
    public void save(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {

        iCabRequestDAO.save(eFmFmEmployeeTravelRequestPO);
    }
    
   

    @Override
    public void save(EFmFmTripTimingMasterPO eFmFmTripTimingMasterPO) {

        iCabRequestDAO.save(eFmFmTripTimingMasterPO);
    }

    @Override
    public void update(EFmFmTripTimingMasterPO eFmFmTripTimingMasterPO) {

        iCabRequestDAO.update(eFmFmTripTimingMasterPO);

    }
    
    @Override
    public void update(EFmCheckInVehicleTrackingPO eFmCheckInVehicleTrackingPO) {
        iCabRequestDAO.update(eFmCheckInVehicleTrackingPO);

    }

    @Override
    public void save(EFmFmAssignRoutePO eFmFmAssignRoutePO) {

        iCabRequestDAO.save(eFmFmAssignRoutePO);
    }

    @Override
    public void update(EFmFmAssignRoutePO eFmFmAssignRoutePO) {

        iCabRequestDAO.update(eFmFmAssignRoutePO);

    }

    @Override
    public void update(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
        iCabRequestDAO.update(eFmFmEmployeeTravelRequestPO);
    }

    @Override
    public void delete(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
        iCabRequestDAO.delete(eFmFmEmployeeTravelRequestPO);

    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequest(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {
        return iCabRequestDAO.listOfTravelRequest(eFmFmEmployeeTravelRequestPO);
    }

    @Override
    public List<EFmFmTripTimingMasterPO> listOfShiftTime(String clientId) {

        return iCabRequestDAO.listOfShiftTime(clientId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getTodayRequestForParticularEmployee(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest) {

        return iCabRequestDAO.getTodayRequestForParticularEmployee(eFmFmEmployeeTravelRequest);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getparticularRequestDetail(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {

        return iCabRequestDAO.getparticularRequestDetail(eFmFmEmployeeTravelRequestPO);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getParticularApproveRequestDetail(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {

        return iCabRequestDAO.getParticularApproveRequestDetail(eFmFmEmployeeTravelRequestPO);
    }

    @Override
    public void save(EFmFmEmployeeRequestMasterPO employeeRequestMasterPO) {

        iCabRequestDAO.save(employeeRequestMasterPO);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getParticularRequestDetail(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {

        return iCabRequestDAO.getparticularRequestDetail(eFmFmEmployeeTravelRequestPO);
    }

    @Override
    public List<EFmFmAssignRoutePO> gettripForParticularDriver(EFmFmAssignRoutePO assignRoutePO) {

        return iCabRequestDAO.gettripForParticularDriver(assignRoutePO);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getParticularTripAllEmployees(int assignRouteId) {

        return iCabRequestDAO.getParticularTripAllEmployees(assignRouteId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getParticularDriverAssignTripDetail(EFmFmAssignRoutePO assignRoutePO) {

        return iCabRequestDAO.getParticularDriverAssignTripDetail(assignRoutePO);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getParticularTripParticularEmployees(int employeeId, int assignRouteId,
            int clientId) {

        return iCabRequestDAO.getParticularTripParticularEmployees(employeeId, assignRouteId, clientId);
    }

    @Override
    public void update(EFmFmEmployeeTripDetailPO employeeTripDetailPO) {

        iCabRequestDAO.update(employeeTripDetailPO);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getTodayTripEmployeesDetail(int employeeId, int clientId, Date todayDate) {

        return iCabRequestDAO.getTodayTripEmployeesDetail(employeeId, clientId, todayDate);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllTodayTripDetails(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest) {

        return iCabRequestDAO.getAllTodayTripDetails(eFmFmEmployeeTravelRequest);
    }

    @Override
    public void update(EFmFmEmployeeRequestMasterPO eFmFmEmployeeRequestMasterPO) {

        iCabRequestDAO.update(eFmFmEmployeeRequestMasterPO);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllTodaysActiveRequests(int clientId) {

        return iCabRequestDAO.getAllTodaysActiveRequests(clientId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getParticularTriprEmployeeBoardedStatus(int requestId, int assignRouteId) {

        return iCabRequestDAO.getParticularTriprEmployeeBoardedStatus(requestId, assignRouteId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllResheduleRequests(int projectId, int branchId) {

        return iCabRequestDAO.getAllResheduleRequests(projectId, branchId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getParticularTripNonDropEmployeesDetails(int assignRouteId) {

        return iCabRequestDAO.getParticularTripNonDropEmployeesDetails(assignRouteId);
    }

    @Override
    public void deleteParticularTripDetail(int empTripId) {

        iCabRequestDAO.deleteParticularTripDetail(empTripId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getrequestStatusFromBranchIdAndRequestId(int branchId, int requestId) {

        return iCabRequestDAO.getrequestStatusFromBranchIdAndRequestId(branchId, requestId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getDropTripAllSortedEmployees(int assignRouteId) {

        return iCabRequestDAO.getDropTripAllSortedEmployees(assignRouteId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllTodaysRequestForParticularEmployee(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest) {

        return iCabRequestDAO.getAllTodaysRequestForParticularEmployee(eFmFmEmployeeTravelRequest);
    }

    @Override
    public List<EFmFmEmployeeRequestMasterPO> getEmplyeeRequestsForSameDateAndShiftTime(Date date, Time siftTime,
            String branchId, int userId, String tripType) {

        return iCabRequestDAO.getEmplyeeRequestsForSameDateAndShiftTime(date, siftTime, branchId, userId, tripType);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(Date date,
            Time siftTime, String branchId, int userId, String tripType) {

        return iCabRequestDAO.getEmplyeeRequestsForSameDateAndShiftTimeFromTravelReq(date, siftTime, branchId, userId,
                tripType);
    }

    @Override
    public void save(EFmFmEmployeeTripDetailPO employeeTripDetailPO) {

        iCabRequestDAO.save(employeeTripDetailPO);
    }

    @Override
    public List<EFmFmEmployeeRequestMasterPO> travelRequestExist(String employeeId, String tripType, int branchId,
            String requestType) {

        return iCabRequestDAO.travelRequestExist(employeeId, tripType, branchId, requestType);
    }

    @Override
    public List<EFmFmEmployeeRequestMasterPO> getRequestFromRequestMaster(int tripId, String branchId) {

        return iCabRequestDAO.getRequestFromRequestMaster(tripId, branchId);
    }

    @Override
    public void deleteParticularRequest(int requestId) {

        iCabRequestDAO.deleteParticularRequest(requestId);
    }

    @Override
    public void deleteParticularRequestFromRequestMaster(int tripId) {

        iCabRequestDAO.deleteParticularRequestFromRequestMaster(tripId);
    }

    @Override
    public List<EFmFmEmployeeRequestMasterPO> getAllRequestDetailsFromRequestMasterFromBranchId(int branchId) {

        return iCabRequestDAO.getAllRequestDetailsFromRequestMasterFromBranchId(branchId);
    }

    @Override
    public List<EFmFmAssignRoutePO> closeVehicleCapacity(int checkInId, int branchId) {

        return iCabRequestDAO.closeVehicleCapacity(checkInId, branchId);
    }

    @Override
    public List<EFmFmEmployeeRequestMasterPO> getAllRequestFromRequestMasterFprParticularEmployee(int userId,
            String branchId) {

        return iCabRequestDAO.getAllRequestFromRequestMasterFprParticularEmployee(userId, branchId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getLastRouteDetails(int checkInId, int branchId, String tripType) {

        return iCabRequestDAO.getLastRouteDetails(checkInId, branchId, tripType);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getParticularRequestDetailOnTripComplete(int requestId) {

        return iCabRequestDAO.getParticularRequestDetailOnTripComplete(requestId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllocatedEmployeeDetail(int userId, String branchId, Date todayDate) {

        return iCabRequestDAO.getAllocatedEmployeeDetail(userId, branchId, todayDate);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> assignCabRequestToParticularShiftOrRouteEmployees(String requestDate,String branchId,
            String tripType, Time siftTime, int zoneId) {

        return iCabRequestDAO.assignCabRequestToParticularShiftOrRouteEmployees(requestDate,branchId, tripType, siftTime, zoneId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllParticularRouteRequest(int branchId, int zoneId, Time shiftTime) {

        return iCabRequestDAO.getAllParticularRouteRequest(branchId, zoneId, shiftTime);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleLessCapacity(String branchId, int capacity) {

        return iCabRequestDAO.getAllCheckedInVehicleLessCapacity(branchId, capacity);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getAllCheckedInVehicleLargeCapacity(String branchId, int capacity) {

        return iCabRequestDAO.getAllCheckedInVehicleLargeCapacity(branchId, capacity);
    }

    @Override
    public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteFromCheckInId(int branchId, int zoneId, String reqType,
            Time shiftTime, int checkInId) {

        return iCabRequestDAO.getHalfCompletedAssignRouteFromCheckInId(branchId, zoneId, reqType, shiftTime, checkInId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getHalfCompletedAssignRoute(int branchId, int zoneId, String reqType,
            Time shiftTime) {

        return iCabRequestDAO.getHalfCompletedAssignRoute(branchId, zoneId, reqType, shiftTime);
    }

    @Override
    public List<EFmFmEscortCheckInPO> getAllCheckedInEscort(String branchId) {

        return iCabRequestDAO.getAllCheckedInEscort(branchId);
    }

    @Override
    public List<EFmFmVehicleMasterPO> getVehicleDetailsFromClientIdForSmallerRoute(int branchId, String vehicleId) {

        return iCabRequestDAO.getVehicleDetailsFromClientIdForSmallerRoute(branchId, vehicleId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getRequestStatusFromBranchIdAndRequestId(String branchId, int requestId) {

        return iCabRequestDAO.getRequestStatusFromBranchIdAndRequestId(branchId, requestId);
    }

    @Override
    public void deleteParticularRequestFromEmployeeTripDetail(int empTripId) {

        iCabRequestDAO.deleteParticularRequestFromEmployeeTripDetail(empTripId);
    }

    @Override
    public List<EFmFmEmployeeRequestMasterPO> getParticularEmployeeMasterRequestDetails(int branchId, int tripId) {

        return iCabRequestDAO.getParticularEmployeeMasterRequestDetails(branchId, tripId);
    }

    @Override
    public EFmFmVehicleMasterPO getVehicleDetail(int vehicleId) {

        return iCabRequestDAO.getVehicleDetail(vehicleId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> assignCabRequestToParticularShiftEmployees(int branchId, String tripType,
            Time siftTime) {

        return iCabRequestDAO.assignCabRequestToParticularShiftEmployees(branchId, tripType, siftTime);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllActiveRequests(int branchId) {

        return iCabRequestDAO.getAllActiveRequests(branchId);
    }

    @Override
    public long getAllActivePickUpRequestCounter(int branchId) {

        return iCabRequestDAO.getAllActivePickUpRequestCounter(branchId);
    }

    @Override
    public long getAllActivePickupInProgressEmployeeRequestCounter(String branchId) {

        return iCabRequestDAO.getAllActivePickupInProgressEmployeeRequestCounter(branchId);
    }

    @Override
    public long getAllActiveNoShowEmployeeRequestCounter(String branchId) {

        return iCabRequestDAO.getAllActiveNoShowEmployeeRequestCounter(branchId);
    }

    @Override
    public long getAllActiveBoardedEmployeeRequestCounter(String branchId) {

        return iCabRequestDAO.getAllActiveBoardedEmployeeRequestCounter(branchId);
    }

    @Override
    public long getAllActiveDropRequestCounter(String branchId) {

        return iCabRequestDAO.getAllActiveDropRequestCounter(branchId);
    }

    @Override
    public long getAllActiveDropedEmployeeRequestCounter(String branchId) {

        return iCabRequestDAO.getAllActiveDropedEmployeeRequestCounter(branchId);
    }

    @Override
    public long getAllActiveDropNoShowEmployeeRequestCounter(String branchId) {

        return iCabRequestDAO.getAllActiveDropNoShowEmployeeRequestCounter(branchId);
    }

    @Override
    public long getAllActiveDropInProgressEmployeeRequestCounter(String branchId) {

        return iCabRequestDAO.getAllActiveDropInProgressEmployeeRequestCounter(branchId);
    }

    @Override
    public long getAllPickupScheduleActiveRequests(String branchId) {

        return iCabRequestDAO.getAllPickupScheduleActiveRequests(branchId);
    }

    @Override
    public long getAllDropScheduleActiveRequests(String branchId) {

        return iCabRequestDAO.getAllDropScheduleActiveRequests(branchId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAnotherActiveRequestDetail(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {

        return iCabRequestDAO.getAnotherActiveRequestDetail(eFmFmEmployeeTravelRequestPO);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAnotherActiveRequestForNextDate(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {

        return iCabRequestDAO.getAnotherActiveRequestForNextDate(eFmFmEmployeeTravelRequestPO);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> particularEmployeeRequestFromEmpId(String branchId, String employeeId) {

        return iCabRequestDAO.particularEmployeeRequestFromEmpId(branchId, employeeId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequestForAdminShiftWise(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {

        return iCabRequestDAO.listOfTravelRequestForAdminShiftWise(eFmFmEmployeeTravelRequestPO);
    }

    @Override
    public long getAllActiveDropOrPickupRequestCounterForGuest(String branchId) {

        return iCabRequestDAO.getAllActiveDropOrPickupRequestCounterForGuest(branchId);
    }

    @Override
    public long getAllActiveNoShowGuestRequestCounter(String branchId) {

        return iCabRequestDAO.getAllActiveNoShowGuestRequestCounter(branchId);
    }

    @Override
    public long getAllActiveBoardedGuestRequestCounter(String branchId) {

        return iCabRequestDAO.getAllActiveBoardedGuestRequestCounter(branchId);
    }

    @Override
    public long getAllScheduleActiveRequestsForGuest(String branchId) {

        return iCabRequestDAO.getAllScheduleActiveRequestsForGuest(branchId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequestByShiftWice(int branchId,String requestDate,Time shiftTime) {

        return iCabRequestDAO.listOfTravelRequestByShiftWice(branchId,requestDate,shiftTime);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> listOfAdhocAndGuestTravelRequests(int branchId) {

        return iCabRequestDAO.listOfAdhocAndGuestTravelRequests(branchId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllDropScheduleActiveRequestsDetails(String branchId) {

        return iCabRequestDAO.getAllDropScheduleActiveRequestsDetails(branchId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllActiveDropedEmployeeRequestsDetails(String branchId) {

        return iCabRequestDAO.getAllActiveDropedEmployeeRequestsDetails(branchId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllActiveDropNoShowEmployeeRequestsDetails(String branchId) {

        return iCabRequestDAO.getAllActiveDropNoShowEmployeeRequestsDetails(branchId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllPickupScheduleActiveRequestsDetails(String branchId) {

        return iCabRequestDAO.getAllPickupScheduleActiveRequestsDetails(branchId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllActivePickupBoardedEmployeeRequestsDetails(String branchId) {

        return iCabRequestDAO.getAllActivePickupBoardedEmployeeRequestsDetails(branchId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllActivePickupNoShowEmployeeRequestsDetails(String branchId) {

        return iCabRequestDAO.getAllActivePickupNoShowEmployeeRequestsDetails(branchId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllActiveDropRequestDetails(int branchId) {

        return iCabRequestDAO.getAllActiveDropRequestDetails(branchId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllActivePickUpRequestDetails(int branchId) {

        return iCabRequestDAO.getAllActivePickUpRequestDetails(branchId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getNonDropTripAllSortedEmployees(int assignRouteId) {

        return iCabRequestDAO.getNonDropTripAllSortedEmployees(assignRouteId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getEmployeeLiveTripDetailFromUserId(int userId, int branchId) {

        return iCabRequestDAO.getEmployeeLiveTripDetailFromUserId(userId, branchId);
    }

    @Override
    public List<EFmFmTripTimingMasterPO> getParticularShiftTimeDetail(int branchId, Time shiftTime) {

        return iCabRequestDAO.getParticularShiftTimeDetail(branchId, shiftTime);
    }

    @Override
    public List<EFmFmTripTimingMasterPO> listOfShiftTimeByTripType(String branchId, String tripType) {

        return iCabRequestDAO.listOfShiftTimeByTripType(branchId, tripType);
    }

    @Override
    public List<EFmFmEmployeeRequestMasterPO> getAllRequestDetailsFromRequestMasterFromBranchIdByTripType(int branchId,
            String tripType) {

        return iCabRequestDAO.getAllRequestDetailsFromRequestMasterFromBranchIdByTripType(branchId, tripType);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllScheduleActiveGuestRequestsDetails(String branchId) {

        return iCabRequestDAO.getAllScheduleActiveGuestRequestsDetails(branchId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllActiveBoardedOrDroppedEmployeeRequestsDetailsForGuest(String branchId) {

        return iCabRequestDAO.getAllActiveBoardedOrDroppedEmployeeRequestsDetailsForGuest(branchId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllActiveGuestRequestsDetails(String branchId) {

        return iCabRequestDAO.getAllActiveGuestRequestsDetails(branchId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllActiveGuestNoShowRequestsDetails(String branchId) {

        return iCabRequestDAO.getAllActiveGuestNoShowRequestsDetails(branchId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> particularEmployeePickupRequestFromUserId(int branchId, int userId,
            String tripType) {

        return iCabRequestDAO.particularEmployeePickupRequestFromUserId(branchId, userId, tripType);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> deleteCurentRequestfromTraveldesk(int branchId, int tripId) {

        return iCabRequestDAO.deleteCurentRequestfromTraveldesk(branchId, tripId);
    }

    @Override
    public List<EFmFmTripTimingMasterPO> getShiftTimeDetailFromShiftTimeAndTripType(String branchId, Time shiftTime,
            String tripType) {

        return iCabRequestDAO.getShiftTimeDetailFromShiftTimeAndTripType(branchId, shiftTime, tripType);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequestByShiftTripType(int branchId, Time shiftTime,
            String tripType, String todaysDate) {

        return iCabRequestDAO.listOfTravelRequestByShiftTripType(branchId, shiftTime, tripType, todaysDate);
    }

    @Override
    public List<EFmFmClientBranchPO> getBranchDetails(int branchId) {

        return iCabRequestDAO.getBranchDetails(branchId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getparticularRequestwithShiftTime(
            EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequestPO) {

        return iCabRequestDAO.getparticularRequestwithShiftTime(eFmFmEmployeeTravelRequestPO);
    }

    @Override
    public List<EFmFmAssignRoutePO> getCreatedAssignRoute(int branchId, String reqType, Time shiftTime) {

        return iCabRequestDAO.getCreatedAssignRoute(branchId, reqType, shiftTime);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getDropTripByAlgoRouteSortedEmployees(int assignRouteId) {

        return iCabRequestDAO.getDropTripByAlgoRouteSortedEmployees(assignRouteId);
    }

    @Override
    public void deleteParticularTripDetailByRouteId(int routeId) {

        iCabRequestDAO.deleteParticularTripDetailByRouteId(routeId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getparticularEmployeeRequest(String todaysDate, String employeeId,
            String tripType, int branchId, String shiftTime) {

        return iCabRequestDAO.getparticularEmployeeRequest(todaysDate, employeeId, tripType, branchId, shiftTime);
    }

    @Override
    public EFmFmEmployeeTripDetailPO ParticularTripDetail(int empTripId) {

        return iCabRequestDAO.ParticularTripDetail(empTripId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getparticularEmployeeTripDetails(String todaysDate, String employeeId,
            String tripType, int branchId, String shiftTime) {

        return iCabRequestDAO.getparticularEmployeeTripDetails(todaysDate, employeeId, tripType, branchId, shiftTime);
    }

    @Override
    public List<EFmFmTripTimingMasterPO> getParticularShiftTimeDetailByTripType(String branchId, Time shiftTime,
            String tripType) {

        return iCabRequestDAO.getParticularShiftTimeDetailByTripType(branchId, shiftTime, tripType);
    }

    @Override
    public List<EFmFmEmployeeRequestMasterPO> getParticularRequestDetailFromUserIdAndTripType(int userId, int branchId,
            String tripType) {

        return iCabRequestDAO.getParticularRequestDetailFromUserIdAndTripType(userId, branchId, tripType);
    }

    @Override
    public List<Time> listOfAllShiftTimesFromTravelDesk(String branchId, String tripType) {

        return iCabRequestDAO.listOfAllShiftTimesFromTravelDesk(branchId, tripType);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getParticularTripEmployeeRequestDetails(int empTripId, int requestId,
            int assignRouteId) {

        return iCabRequestDAO.getParticularTripEmployeeRequestDetails(empTripId, requestId, assignRouteId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getTripAllEmployeesFromAssignRouteIdBranchId(int assignRouteId,
            int branchId) {

        return iCabRequestDAO.getTripAllEmployeesFromAssignRouteIdBranchId(assignRouteId, branchId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getCoPassenger(int assignRouteId, int requestId) {

        return iCabRequestDAO.getCoPassenger(assignRouteId, requestId);
    }

    @Override
    public List<EFmFmEmployeeRequestMasterPO> getActiveRequestDetailsFromBranchIdAndUserId(int branchId, int userId) {

        return iCabRequestDAO.getActiveRequestDetailsFromBranchIdAndUserId(branchId, userId);
    }

    @Override
    public List<EFmFmVehicleCheckInPO> getAllCheckedInVehiclesForSpecificCapacity(String branchId, int capacity) {

        return iCabRequestDAO.getAllCheckedInVehiclesForSpecificCapacity(branchId, capacity);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> assignCabToParticularShiftOrDateEmployees(String requestDate,
            int branchId, String tripType, Time siftTime) {

        return iCabRequestDAO.assignCabToParticularShiftOrDateEmployees(requestDate, branchId, tripType, siftTime);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> assignCabToParticularShiftDateOrRouteEmployees(String requestDate,
    		String branchId, String tripType, Time siftTime, int zoneId) {

        return iCabRequestDAO.assignCabToParticularShiftDateOrRouteEmployees(requestDate, branchId, tripType, siftTime,
                zoneId);
    }

    @Override
    public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteByDate(String requestDate, int branchId, int zoneId,
            String reqType, Time shiftTime) {

        return iCabRequestDAO.getHalfCompletedAssignRouteByDate(requestDate, branchId, zoneId, reqType, shiftTime);
    }

    @Override
    public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteFromCheckInIdByDate(String requestDate, int branchId,
            int zoneId, String reqType, Time shiftTime, int checkInId) {

        return iCabRequestDAO.getHalfCompletedAssignRouteFromCheckInIdByDate(requestDate, branchId, zoneId, reqType,
                shiftTime, checkInId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllParticularRouteRequestByDate(String requestDate, int branchId,
            int zoneId, Time shiftTime) {

        return iCabRequestDAO.getAllParticularRouteRequestByDate(requestDate, branchId, zoneId, shiftTime);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getDropTripAllSortedNonDropEmployees(int assignRouteId) {

        return iCabRequestDAO.getDropTripAllSortedNonDropEmployees(assignRouteId);
    }

    @Override
    public List<EFmFmEmployeeTripDetailPO> getAllocatedEmployeeDetailFromUserIdAndBranchId(int userId, int branchId) {

        return iCabRequestDAO.getAllocatedEmployeeDetailFromUserIdAndBranchId(userId, branchId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getAllUpComingRequestForParticularEmployeeTripBased(int branchId,
            int userId, String tripType) {
        
        return iCabRequestDAO.getAllUpComingRequestForParticularEmployeeTripBased(branchId, userId, tripType);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getParticularRequestFromRequestDateAndUserId(Date todayDate,String tripType, int userId,
            int branchId) {
        
        return iCabRequestDAO.getParticularRequestFromRequestDateAndUserId(todayDate,tripType, userId, branchId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> getParticularEmployeeLastRequestFromUserId(String tripType, int userId,
            int branchId) {
        
        return iCabRequestDAO.getParticularEmployeeLastRequestFromUserId(tripType, userId, branchId);
    }

    @Override
    public List<EFmFmEmployeeTravelRequestPO> listOfTravelRequestByShiftWiceForNormalAndAdhocRequests(int branchId,
            String requestDate, Time shiftTime) {
        
        return iCabRequestDAO.listOfTravelRequestByShiftWiceForNormalAndAdhocRequests(branchId, requestDate, shiftTime);
    }

	@Override
	public List<EFmFmTripTimingMasterPO> getParticularShiftTimeDetailByShiftId(int branchId, int shifId) {
		
		return iCabRequestDAO.getParticularShiftTimeDetailByShiftId(branchId, shifId);
	}

    @Override
    public List<EFmFmEmployeeTravelRequestPO> listOfAdhocReservationsForGuestTravelRequests(int branchId) {
        
        return iCabRequestDAO.listOfAdhocReservationsForGuestTravelRequests(branchId);
    }

	

	@Override
	public List<EFmFmEmployeeTravelRequestPO> particularDateRequestForEmployees(Date date, int branchId, int userId,
			String tripType) {
		
		return iCabRequestDAO.particularDateRequestForEmployees(date, branchId, userId, tripType);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> listOfGuestTravelRequests(int branchId) {
		
		return iCabRequestDAO.listOfGuestTravelRequests(branchId);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getNextWeekRequestForParticularEmployee(
			EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest) {
		return iCabRequestDAO.getNextWeekRequestForParticularEmployee(eFmFmEmployeeTravelRequest);
	}

	@Override
	public List<EFmFmEmployeeRequestMasterPO> getActiveEmplyeeRequest(int branchId, int userId, String tripType) {
		return iCabRequestDAO.getActiveEmplyeeRequest(branchId, userId, tripType);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> assignCabToPickupShiftOrDateEmployees(String requestDate, String branchId,
			String tripType, Time siftTime) {
		return iCabRequestDAO.assignCabToPickupShiftOrDateEmployees(requestDate, branchId, tripType, siftTime);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> assignCabToDropShiftOrDateEmployees(String requestDate, String branchId,
			String tripType, Time siftTime) {
		return iCabRequestDAO.assignCabToDropShiftOrDateEmployees(requestDate, branchId, tripType, siftTime);
	}

	@Override
	public List<EFmFmEmployeeRequestMasterPO> getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(Date date,
			Time siftTime, String branchId, int userId, String tripType) {
		return iCabRequestDAO.getEmplyeeRequestsForSameDateAndShiftTimeOnRoster(date, siftTime, branchId, userId, tripType);
	}

	@Override
	public List<EFmFmTripTimingMasterPO> getShiftIdUsingshiftTime(int branchId, String tripType, Time siftTime) {
		return iCabRequestDAO.getShiftIdUsingshiftTime(branchId, tripType, siftTime);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> listOfGuestAndAdhocRequestTravelRequests(int branchId) {
		return iCabRequestDAO.listOfGuestAndAdhocRequestTravelRequests(branchId);
	}

	

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getAllRequestForParticularEmployeeBasedOnMobileNumber(String branchId,
			String mobileNumber,int startPgNo,int endPgNo) {
		return iCabRequestDAO.getAllRequestForParticularEmployeeBasedOnMobileNumber(branchId, mobileNumber,startPgNo,endPgNo);
	}

	@Override
	public List<EFmFmTripTimingMasterPO> listOfShiftTimeForEmployees(String branchId) {
		return iCabRequestDAO.listOfShiftTimeForEmployees(branchId);
	}

	@Override
	public List<EFmFmTripTimingMasterPO> listOfShiftTimeByTripTypeForEmployees(int branchId, String tripType) {
		return iCabRequestDAO.listOfShiftTimeByTripTypeForEmployees(branchId, tripType);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getAllUpComingRequestForParticularEmployee(int branchId, int userId,
			String tripType) {
		return iCabRequestDAO.getAllUpComingRequestForParticularEmployee(branchId, userId, tripType);
	}

	@Override
	public List<EFmFmEmployeeRequestMasterPO> getAllActiveRequestDetailsFromRequestMasterdByBranchIdAndTripType(
			String branchId, String tripType) {
		return iCabRequestDAO.getAllActiveRequestDetailsFromRequestMasterdByBranchIdAndTripType(branchId, tripType);
	}

	@Override
	public List<EFmFmEmployeeRequestMasterPO> getActiveRequestDetailsFromBranchIdAndUserIdForAnEmployee(String branchId,
			int userId) {
		return iCabRequestDAO.getActiveRequestDetailsFromBranchIdAndUserIdForAnEmployee(branchId, userId);
	}

	@Override
	public long getAllActivePickUpRequestCounterForTodays(String branchId, String todayDate) {
		return iCabRequestDAO.getAllActivePickUpRequestCounterForTodays(branchId, todayDate);
	}

	@Override
	public long getAllActiveDropRequestCounterForTodays(String branchId, String todayDate) {
		return iCabRequestDAO.getAllActiveDropRequestCounterForTodays(branchId, todayDate);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getAllActivePickUpRequestDetailsForToday(String branchId,
			String requestDate) {
		return iCabRequestDAO.getAllActivePickUpRequestDetailsForToday(branchId, requestDate);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getAllActiveDropRequestDetailsForToday(String branchId, String requestDate) {
		return iCabRequestDAO.getAllActiveDropRequestDetailsForToday(branchId, requestDate);
	}

	@Override
	public List<EFmFmAssignRoutePO> getStartedVehicleDetailFromVehicleNumber(int checkInId, String branchId) {
		return iCabRequestDAO.getStartedVehicleDetailFromVehicleNumber(checkInId, branchId);
	}

	@Override
	public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteByRouteName(String requestDate, int branchId, int zoneId,
			String tripType, Time shiftTime, String string) {
		return iCabRequestDAO.getHalfCompletedAssignRouteByRouteName(requestDate, branchId, zoneId, tripType, shiftTime, string);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getParticularTripAllEmployeesDesc(int assignRouteId) {
		return iCabRequestDAO.getParticularTripAllEmployeesDesc(assignRouteId);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getRouteEmployeeStatus(int assignRouteId) {
		return iCabRequestDAO.getRouteEmployeeStatus(assignRouteId);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getParticularTripEmployees(int assignRouteId) {
		return iCabRequestDAO.getParticularTripEmployees(assignRouteId);
	}

	@Override
	public List<EFmFmEmployeeRequestMasterPO> getEmployeeRequestDetails(String branchId, int UserId) {
		return iCabRequestDAO.getEmployeeRequestDetails(branchId, UserId);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestValidation(Date date, Date toDate, int branchId,
			int userId, String tripType) {
		return iCabRequestDAO.employeeTravelRequestValidation(date, toDate, branchId, userId, tripType);
	}

	@Override
	public List<EFmFmTripTimingMasterPO> getCeilingShiftTimeDetailByShiftId(int branchId, int shiftId) {
		return iCabRequestDAO.getCeilingShiftTimeDetailByShiftId(branchId, shiftId);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getEmployeeRequestCeilingCount(int branchId, String requestDate,
			String shiftTime, String tripType) {
		return iCabRequestDAO.getEmployeeRequestCeilingCount(branchId, requestDate, shiftTime, tripType);
	}

	@Override
	public List<EFmFmEmployeeRequestMasterPO> getEmployeeRequestCeiling(int branchId, String requestDate,
			String shiftTime, String tripType) {
		return iCabRequestDAO.getEmployeeRequestCeiling(branchId, requestDate, shiftTime, tripType);
	}

	@Override
	public List<EFmFmEmployeeRequestMasterPO> getParticularRequestDetailFromUserIdByRead(int userId, int branchId,
			String tripType) {
		return iCabRequestDAO.getParticularRequestDetailFromUserIdByRead(userId, branchId, tripType);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> pastRequestByTravelMaster(int tripId) {
		return iCabRequestDAO.pastRequestByTravelMaster(tripId);
	}

	@Override
	public List<EFmFmEmployeeRequestMasterPO> getRequestMasterDetailsByTripId(int tripId) {
		return iCabRequestDAO.getRequestMasterDetailsByTripId(tripId);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getLastDropEmployeeDetail(int assignRouteId) {
		return iCabRequestDAO.getLastDropEmployeeDetail(assignRouteId);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> employeeTravelledRequestValidation(Date date, Date toDate, int branchId,
			int userId, String tripType) {
		return iCabRequestDAO.employeeTravelledRequestValidation(date, toDate, branchId, userId, tripType);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getParticularActiveRequestDetail(int userId, String tripType,
			String requestDate, Time shiftTime) {
		return iCabRequestDAO.getParticularActiveRequestDetail(userId, tripType, requestDate, shiftTime);
	}

	@Override
	public List<EFmFmVehicleCheckInPO> getAllCheckedInDummyVehicles(String branchId) {
		return iCabRequestDAO.getAllCheckedInDummyVehicles(branchId);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getAllWithOutNoShowDropTripEmployees(int assignRouteId) {
		return iCabRequestDAO.getAllWithOutNoShowDropTripEmployees(assignRouteId);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getAllWithOutNoShowPickupTripEmployees(int assignRouteId) {
		return iCabRequestDAO.getAllWithOutNoShowPickupTripEmployees(assignRouteId);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> assignCabByLocationFlg(String requestDate, String branchId, String tripType,
			Time siftTime) {		
		return iCabRequestDAO.assignCabByLocationFlg(requestDate, branchId, tripType, siftTime);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> assignCabToParticularShiftDateByLocationFlg(String requestDate,
			String branchId, String tripType, Time siftTime, int zoneId,String locationFlg) {	
		return iCabRequestDAO.assignCabToParticularShiftDateByLocationFlg(requestDate, branchId, tripType, siftTime, zoneId,locationFlg);
	}

	@Override
	public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteByLocationFlg(String tripAssignDate, int branchId,
			int zoneId, String reqType, Time shiftTime, String locationFlg) {	
		return iCabRequestDAO.getHalfCompletedAssignRouteByLocationFlg(tripAssignDate, branchId, zoneId, reqType, shiftTime, locationFlg);
	}

	@Override
	public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteByDateWithNormalFlg(String tripAssignDate, int branchId,
			int zoneId, String reqType, Time shiftTime) {		
		return iCabRequestDAO.getHalfCompletedAssignRouteByDateWithNormalFlg(tripAssignDate, branchId, zoneId, reqType, shiftTime);
	}

	@Override
	public void save(EFmFmMultipleLocationTvlReqPO eFmFmMultipleLocationTvlReqPO) {
		iCabRequestDAO.save(eFmFmMultipleLocationTvlReqPO);
		
	}

	@Override
	public void modify(EFmFmMultipleLocationTvlReqPO eFmFmMultipleLocationTvlReqPO) {
		iCabRequestDAO.modify(eFmFmMultipleLocationTvlReqPO);
		
	}

	@Override
	public List<EFmFmMultipleLocationTvlReqPO> getLocationTravelDetails(int locTvlId, int branchId) {
		return iCabRequestDAO.getLocationTravelDetails(locTvlId, branchId);
	}

	@Override
	public List<EFmFmMultipleLocationTvlReqPO> getLocationTravelDetailsByRequestId(int requestId) {
		return iCabRequestDAO.getLocationTravelDetailsByRequestId(requestId);
	}

	@Override
	public int getRequestId(EFmFmEmployeeRequestMasterPO employeeRequestMasterPO) {
		return iCabRequestDAO.getRequestId(employeeRequestMasterPO);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getlistOfRequestByAssignedId(int assignRouteId) {
		return iCabRequestDAO.getlistOfRequestByAssignedId(assignRouteId);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getEmpMultipleTravelledRequest(int branchId, String requestDate) {
		return iCabRequestDAO.getEmpMultipleTravelledRequest(branchId, requestDate);
	}

	@Override
	public long getAllocatedCountReuestsByShiftWice(int branchId, String requestDate, Time shiftTime, String tripType) {
		return iCabRequestDAO.getAllocatedCountReuestsByShiftWice(branchId, requestDate, shiftTime, tripType);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> assignCabRequestToParticularShiftOrRouteEmployeesByLocationFlg(
			String requestDate, String branchId, String tripType, Time siftTime, int zoneId) {
		return iCabRequestDAO.assignCabRequestToParticularShiftOrRouteEmployeesByLocationFlg(requestDate, branchId, tripType, siftTime, zoneId);
	}



	@Override
	public List<EFmFmEmployeeRequestMasterPO> getListOfBulkApprovalPendingRequest(int branchId, int approvalUserId,
			String approvalFlg) {
		return iCabRequestDAO.getListOfBulkApprovalPendingRequest(branchId, approvalUserId, approvalFlg);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> employeeRequestFromEmpIdByLocationFlg(String branchId, String employeeId) {
		return iCabRequestDAO.employeeRequestFromEmpIdByLocationFlg(branchId, employeeId);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForLocationFlg(int branchId, String requestDate,
			Time shiftTime, String tripType) {
		return iCabRequestDAO.getRewquestByShiftWiceForLocationFlg(branchId, requestDate, shiftTime, tripType);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getAllNormalAndAdhocRequest(int branchId, String requestDate,
			String tripType) {
		return iCabRequestDAO.getAllNormalAndAdhocRequest(branchId, requestDate, tripType);
	}

	@Override
	public void save(EFmFmIndicationMasterPO eFmFmIndicationMasterPO) {
		iCabRequestDAO.save(eFmFmIndicationMasterPO);		
	}

	@Override
	public List<EFmFmIndicationMasterPO> getIndicationDetailsExist(int branchId, String alertTypeRequest,
			String alertFunctionlityType, String levelType, String tiggerTime, int userId) {
		return iCabRequestDAO.getIndicationDetailsExist(branchId, alertTypeRequest, alertFunctionlityType, levelType, tiggerTime, userId);
	}

	@Override
	public List<EFmFmIndicationMasterPO> getAllIndicationDetails(int branchId, String isActive) {
		return iCabRequestDAO.getAllIndicationDetails(branchId, isActive);
	}

	@Override
	public List<EFmFmIndicationMasterPO> getAllAlertFunctionlityType(int branchId) {
		return iCabRequestDAO.getAllAlertFunctionlityType(branchId);
	}

	@Override
	public List<EFmFmIndicationMasterPO> getAllLevelType(int branchId, String getAllAlertFunctionlityType) {
		return iCabRequestDAO.getAllLevelType(branchId, getAllAlertFunctionlityType);
	}

	@Override
	public List<EFmFmIndicationMasterPO> getAllAlertFunctionlityTypeByLevelType(int branchId,
			String getAllAlertFunctionlityType, String levelType) {
		return iCabRequestDAO.getAllAlertFunctionlityTypeByLevelType(branchId, getAllAlertFunctionlityType, levelType);
	}

	@Override
	public List<EFmFmIndicationMasterPO> getAllIndicationById(int branchId, int indicationId) {
		return iCabRequestDAO.getAllIndicationById(branchId, indicationId);
	}

	@Override
	public void update(EFmFmIndicationMasterPO eFmFmIndicationMasterPO) {
		iCabRequestDAO.update(eFmFmIndicationMasterPO);
		
	}
	

	@Override
	public List<EFmFmEmployeeTravelRequestPO> routingByAreaSequencing(String requestDate, int branchId, String tripType,
			Time siftTime) {
		return iCabRequestDAO.routingByAreaSequencing(requestDate, branchId, tripType, siftTime);
	}

	

	@Override
	public List<EFmFmEmployeeTripDetailPO> getAllocatedEmployeeDetailYetToBoard(int userId, int branchId,
			Date todayDate) {
		return iCabRequestDAO.getAllocatedEmployeeDetailYetToBoard(userId, branchId, todayDate);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getEmployeeLiveTripDetailFromUserIdBeforeOnBoard(int userId, int branchId) {
		return iCabRequestDAO.getEmployeeLiveTripDetailFromUserIdBeforeOnBoard(userId, branchId);
	}

	@Override
	public List<EFmFmEmployeeRequestMasterPO> getParticularRequestDetailFromUserIdByShiftTime(int userId, int branchId,
			String tripType, Time shiftTime) {
		return iCabRequestDAO.getParticularRequestDetailFromUserIdByShiftTime(userId, branchId, tripType, shiftTime);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> employeeTravelRequestValidationByGuest(Date date, Date toDate,
			int branchId, int userId, String tripType, Time shiftTime) {
		return iCabRequestDAO.employeeTravelRequestValidationByGuest(date, toDate, branchId, userId, tripType, shiftTime);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> employeeTravelledRequestValidationByGuest(Date date, Date toDate,
			int branchId, int userId, String tripType, Time shiftTime) {
		return iCabRequestDAO.employeeTravelledRequestValidationByGuest(date, toDate, branchId, userId, tripType, shiftTime);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getAllocatedTripDetails(int userId, String branchId) {
		return iCabRequestDAO.getAllocatedTripDetails(userId, branchId);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForNormalAndAdhoc(String branchId,
			String requestDate, Time shiftTime, String tripType) {
		return iCabRequestDAO.getRewquestByShiftWiceForNormalAndAdhoc(branchId, requestDate, shiftTime, tripType);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getGuestAndAdhocTravelRequestsForGivendate(String branchId,
			String requestDate) {
		return iCabRequestDAO.getGuestAndAdhocTravelRequestsForGivendate(branchId, requestDate);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getListOfApprovalPendingRequestForUser(String branchId, int userId,
			String approvalFlg) {
		return iCabRequestDAO.getListOfApprovalPendingRequestForUser(branchId, userId, approvalFlg);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getListOfApprovalPendingRequest(String branchId, int approvalUserId,
			String approvalFlg) {
		return iCabRequestDAO.getListOfApprovalPendingRequest(branchId, approvalUserId, approvalFlg);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhoc(String branchId, String requestDate,
			String tripType) {
		return iCabRequestDAO.getRequestByDayWiceForNormalAndAdhoc(branchId, requestDate, tripType);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceBothPickAndDropForNormalAndAdhoc(String branchId,
			String requestDate) {
		return iCabRequestDAO.getRequestByDayWiceBothPickAndDropForNormalAndAdhoc(branchId, requestDate);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForNormalAndAdhocByManager(int approvalUserId,
			String branchId, String requestDate, Time shiftTime, String tripType) {
		return iCabRequestDAO.getRewquestByShiftWiceForNormalAndAdhocByManager(approvalUserId, branchId, requestDate, shiftTime, tripType);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhocByManager(String branchId,
			int approvalUserId, String requestDate, String tripType) {
		return iCabRequestDAO.getRequestByDayWiceForNormalAndAdhocByManager(branchId, approvalUserId, requestDate, tripType);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceBothPickAndDropForNormalAndAdhocByManager(
			String branchId, int approvalUserId, String requestDate) {
		return iCabRequestDAO.getRequestByDayWiceBothPickAndDropForNormalAndAdhocByManager(branchId, approvalUserId, requestDate);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRequestByShiftWiceForNormalAndAdhocWithoutTripType(String branchId,
			String requestDate, Time shiftTime) {
		return iCabRequestDAO.getRequestByShiftWiceForNormalAndAdhocWithoutTripType(branchId, requestDate, shiftTime);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhocByManagerWithoutTripType(
			String branchId, int approvalUserId, String requestDate, Time shiftTime) {
		return iCabRequestDAO.getRequestByDayWiceForNormalAndAdhocByManagerWithoutTripType(branchId, approvalUserId, requestDate, shiftTime);
	}

	

	@Override
	public Integer getBiggestVehicleTypeCapacity(String branchId) {
		return iCabRequestDAO.getBiggestVehicleTypeCapacity(branchId);
	}

	

	@Override
	public Integer getLowestVehicleTypeCapacityLessThan(String branchId, int capacity) {
		return iCabRequestDAO.getLowestVehicleTypeCapacityLessThan(branchId, capacity);
	}

	@Override
	public Integer getGreatestVehicleTypeCapacity(String branchId, int capacity) {
		return iCabRequestDAO.getGreatestVehicleTypeCapacity(branchId, capacity);
	}

	@Override
	public List<EFmFmVehicleCheckInPO> getAllDummyCheckedInVehiclesForGreatestCapacity(String branchId, int capacity) {
		return iCabRequestDAO.getAllDummyCheckedInVehiclesForGreatestCapacity(branchId, capacity);
	}

	@Override
	public List<EFmFmVehicleCheckInPO> getAllDummyCheckedInVehiclesForLessCapacity(String branchId, int capacity) {
		return iCabRequestDAO.getAllDummyCheckedInVehiclesForLessCapacity(branchId, capacity);
	}
	
	@Override
	public void save(EFmCheckInVehicleTrackingPO eFmCheckInVehicleTrackingPO) {
		iCabRequestDAO.save(eFmCheckInVehicleTrackingPO);
		
	}

	@Override
	public Integer getLowestVehicleTypeCapacity(String branchId) {
		return iCabRequestDAO.getLowestVehicleTypeCapacity(branchId);
	}

	@Override
	public List<EFmFmEmployeeTripDetailPO> getParticularTripModificationDetails(int assignRouteId) {
		return iCabRequestDAO.getParticularTripModificationDetails(assignRouteId);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> tripRequestDetailsByTravelMaster(int requestId) {
		return iCabRequestDAO.tripRequestDetailsByTravelMaster(requestId);
	}

	@Override
	public List<Integer> getAllCapacitiesOfTheVehicles(String combinedFacility) {
		return iCabRequestDAO.getAllCapacitiesOfTheVehicles(combinedFacility);
	}

	@Override
	public List<EFmFmEmployeeRequestMasterPO> getBulkRequestByTripId(int tripId) {
		return iCabRequestDAO.getBulkRequestByTripId(tripId);
	}

	@Override
	public List<EFmFmVehicleCheckInPO> getParticularCapacityDummyCheckedInVehicle(String branchId, int capacity) {
		return iCabRequestDAO.getParticularCapacityDummyCheckedInVehicle(branchId, capacity);
	}

	@Override
	public long assignCabCountToParticularShiftDateOrRouteEmployees(String requestDate, String branchId,
			String tripType, Time siftTime, int zoneId) {
		return iCabRequestDAO.assignCabCountToParticularShiftDateOrRouteEmployees(requestDate, branchId, tripType, siftTime, zoneId);
	}

	@Override
	public long assignCabCountToParticularShiftDateByLocationFlg(String requestDate, String branchId, String tripType,
			Time siftTime, int zoneId, String locationFlg) {
		return iCabRequestDAO.assignCabCountToParticularShiftDateByLocationFlg(requestDate, branchId, tripType, siftTime, zoneId, locationFlg);
	}

	@Override
	public EFmFmVehicleCheckInPO getCheckedInSingleVehicleForSpecificCapacity(String branchId, int capacity) {
		return iCabRequestDAO.getCheckedInSingleVehicleForSpecificCapacity(branchId, capacity);
	}

	@Override
	public EFmFmVehicleCheckInPO getCheckedInSingleVehicleForSpecifiLessCapacity(String branchId, int capacity) {
		return iCabRequestDAO.getCheckedInSingleVehicleForSpecifiLessCapacity(branchId, capacity);
	}

	@Override
	public EFmFmVehicleCheckInPO getCheckedInSingleVehicleForSpecificGreatestCapacity(String branchId, int capacity) {
		return iCabRequestDAO.getCheckedInSingleVehicleForSpecificGreatestCapacity(branchId, capacity);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForNormalAndAdhoc(String branchId,
			String requestDate, Time shiftTime, String tripType, int startPgNo, int endPgNo) {
		return iCabRequestDAO.getRewquestByShiftWiceForNormalAndAdhoc(branchId, requestDate, shiftTime, tripType, startPgNo, endPgNo);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhoc(String branchId, String requestDate,
			String tripType, int startPgNo, int endPgNo) {
		return iCabRequestDAO.getRequestByDayWiceForNormalAndAdhoc(branchId, requestDate, tripType, startPgNo, endPgNo);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceBothPickAndDropForNormalAndAdhoc(String branchId,
			String requestDate, int startPgNo, int endPgNo) {
		return iCabRequestDAO.getRequestByDayWiceBothPickAndDropForNormalAndAdhoc(branchId, requestDate, startPgNo, endPgNo);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceForNormalAndAdhocByManager(String branchId,
			int approvalUserId, String requestDate, String tripType, int startPgNo, int endPgNo) {
		return iCabRequestDAO.getRequestByDayWiceForNormalAndAdhocByManager(branchId, approvalUserId, requestDate, tripType, startPgNo, endPgNo);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRequestByDayWiceBothPickAndDropForNormalAndAdhocByManager(
			String branchId, int approvalUserId, String requestDate, int startPgNo, int endPgNo) {
		return iCabRequestDAO.getRequestByDayWiceBothPickAndDropForNormalAndAdhocByManager(branchId, approvalUserId, requestDate, startPgNo, endPgNo);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForNormalAndAdhocByManager(int approvalUserId,
			String branchId, String requestDate, Time shiftTime, String tripType, int startPgNo, int endPgNo) {
		return iCabRequestDAO.getRewquestByShiftWiceForNormalAndAdhocByManager(approvalUserId, branchId, requestDate, shiftTime, tripType, startPgNo, endPgNo);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getRewquestByShiftWiceForLocationFlg(String branchId, String requestDate,
			Time shiftTime, String tripType, int startPgNo, int endPgNo) {
		return iCabRequestDAO.getRewquestByShiftWiceForLocationFlg(branchId, requestDate, shiftTime, tripType, startPgNo, endPgNo);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getEmpMultipleTravelledRequest(String branchId, String requestDate,
			int startPgNo, int endPgNo) {
		return iCabRequestDAO.getEmpMultipleTravelledRequest(branchId, requestDate, startPgNo, endPgNo);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getGuestAndAdhocTravelRequestsForGivendate(String branchId,
			String requestDate, int startPgNo, int endPgNo) {
		return iCabRequestDAO.getGuestAndAdhocTravelRequestsForGivendate(branchId, requestDate, startPgNo, endPgNo);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> getListOfApprovalPendingRequest(String branchId, int approvalUserId,
			String approvalFlg, int startPgNo, int endPgNo) {	
		return iCabRequestDAO.getListOfApprovalPendingRequest(branchId, approvalUserId, approvalFlg, startPgNo, endPgNo);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> listOfAdhocReservationsForGuestTravelRequests(String branchId,
			int startPgNo, int endPgNo) {		
		return iCabRequestDAO.listOfAdhocReservationsForGuestTravelRequests(branchId, startPgNo, endPgNo);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForPickupRouting(String requestDate,
            String branchId, String tripType, Time siftTime) {
		return iCabRequestDAO.listOfEmployeeByShiftWiseForPickupRouting(requestDate, branchId, tripType, siftTime);
	}

	@Override
	public List<EFmFmEmployeeTravelRequestPO> listOfEmployeeByShiftWiseForDropRouting(String requestDate,
            String branchId, String tripType, Time siftTime) {
		return iCabRequestDAO.listOfEmployeeByShiftWiseForDropRouting(requestDate, branchId, tripType, siftTime);
	}

	@Override
	public List<EFmFmAssignRoutePO> getHalfCompletedAssignRouteForAutoRouting(String tripAssignDate, int branchId,
			String reqType, Time shiftTime, int checkInId) {
		return iCabRequestDAO.getHalfCompletedAssignRouteForAutoRouting(tripAssignDate, branchId, reqType, shiftTime, checkInId);
	}

	

			
}
