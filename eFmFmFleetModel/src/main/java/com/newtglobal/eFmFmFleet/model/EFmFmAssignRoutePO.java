package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The persistent class for the eFmFmAssignRoute database table.
 * 
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name="eFmFmAssignRoute")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EFmFmAssignRoutePO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="AssignRouteId", length=15)
	private int assignRouteId;
	
	@Column(name="SelectedBaseFacility", length=15)
    private int selectedBaseFacility;

	@Column(name="EscortRequredFlag", length=10)
	private String escortRequredFlag;

	@Column(name="OdometerEndKm", length=15)
	private String odometerEndKm;

	@Column(name="OdometerStartKm", length=15)
	private String odometerStartKm;

	@Column(name="PlannedDistance", length=15)
	private double plannedDistance;
	
	
	@Column(name="PlannnedTime", length=15)
	private long plannedTime;
	
	
	@Column(name="PlannedTravelledDistance", length=15)
	private double plannedTravelledDistance;
	
	@Column(name="PlannedReadFlg", length=15)
	private String plannedReadFlg;
	
	
	@Column(name="IsToll", length=15)
	private String isToll;
	
	
	@Column(name="TripConfirmationFromDriver", length=100)
	private String tripConfirmationFromDriver;
	
	@Column(name="ReasonForDelay", length=255)
	private String reasonForDelay;
	
	@Column(name="ScheduleReadFlg", length=15)
	private String scheduleReadFlg;

	@Column(name="RouteGenerationType", length=100)
	private String routeGenerationType;

	@Column(name="NodalPoints")
	private String nodalPoints;
	
	
	@Column(name="TravelledDistance", length=15)
	private double travelledDistance;
	
	@Column(name="EditedTravelledDistance", length=15)
    private double editedTravelledDistance;
	
	@Column(name="EditDistanceApproval")
    private String editDistanceApproval;
	
	@Column(name="IsBackTwoBack", length=100)
    private String isBackTwoBack;
	
	@Column(name="RoutingCompletionStatus", length=100)
	private String routingCompletionStatus;
	
	
	
	@Column(name="BackTwoBackRouteId", length=15)
	private int backTwoBackRouteId;
	
	
	@Column(name="AssignedVendorName", length=200)
	private String assignedVendorName;
	
	
	@Column(name="RemarksForEditingTravelledDistance")
    private String remarksForEditingTravelledDistance;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TripCompleteTime", length=30)
	private Date tripCompleteTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TripAssignDate", length=30)
	private Date tripAssignDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreatedDate", length=30)
	private Date createdDate;
	
	@Column(name="CreatedBy", length=15)
    private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AllocationMsgDeliveryDate", length=30)
	private Date allocationMsgDeliveryDate;
	
	@Column(name="RouteName", length=50)
	private String routeName;
	
	@Column(name="VehicleStatus", length=15)
	private String vehicleStatus;
	
	@Column(name="ApprovedBy", length=15)
    private String approvedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="ApprovedDate", length=30)
    private Date approvedDate;
	
	@Column(name="CabAllocationMessage", length=15)
	private String allocationMsg;
	
	@Column(name="FuelKmAmount",columnDefinition="Decimal(10,1) default '0.0'", length=50)
	private double fuelKmAmount;
	
	@Column(name="DistanceUpdationFlg", length=100)
	private String distanceUpdationFlg;
	
	@Column(name="LocationFlg", length=10)
	private String locationFlg;
	
	@Column(name="EditDistanceType", length=10)
	private String editDistanceType;
	
	@Column(name="TripSheetModifiedStatus", length=200)
	private String tripSheetModifiedStatus;


	@Transient
	private String toDate;
	
	@Transient
	private String fromDate;
	
	@Transient
	private String boardedFlg;
	
	@Transient
	private String searchType;
	
	@Transient
	private String advanceFlag;
	
	@Transient
	private int vehicleId;
	
	@Transient
	private int vendorId;
	
	@Transient
	private int driverId;
	
	@Transient
	private int zoneId;
	
	@Transient
	private String employeeId;
	
	@Transient
	private int requestId;
	
	@Transient
    private int branchId;
	
	@Transient
    private String bulkRouteIds;
	
	@Transient
    private String bulkCheckInIds;
	
	@Transient
	private int tripId;
	
	@Transient
	private int deviceId;
	
	@Transient
	private int nodalPointId;
	
	@Transient
	private int escortCheckInId;
	
	@Transient
	private int newCheckInId;
	
	@Transient
	private int selectedAssignRouteId;
	
	@Transient
	private String requestType;
	
	
	@Transient
	private String vehicleNumber;
	
	@Transient
	private String vendorIdSelected;	

	@Transient
	private String time;
	
	/*dynamic Report*/
	@Transient
	private int routeIdFlg;
	@Transient
	private int assignDateFlg;
	@Transient
	private int routeCloseTimeFlg;
	@Transient
	private int tripStartTimeFlg;
	@Transient
	private int cabReachedTimeFlg;
	@Transient
	private int boardingStatusFlg;
	@Transient
	private int boardingTimeFlg;
	@Transient
	private int tripEndTimeFlg;	
	@Transient
	private int pickUpTimeFlg;
	@Transient
	private int shiftTimeFlg;
	@Transient
	private int tripTypeFlg;
	@Transient
	private int driverNameFlg;	
	@Transient
	private int driverIdFlg;
	@Transient
	private int vehicleNumberFlg;
	@Transient
	private int routeNameFlg;
	@Transient
	private int driverMobileNumberFlg;
	@Transient
	private int vendorNameFlg;
	
	@Transient
	private int remarksFlg;
	
	@Transient
	private int escortFlg;
	
	@Transient
	private int plannedDistanceFlg;
	@Transient
	private int gpsFlg;
	
	@Transient
	private int totalWorkingHoursFlg;
	
	@Transient
	private int driverDrivingHoursPerTripFlg;
	
	@Transient
	private int totalDrivingHoursFlg;	
	
	@Transient
	private int checkInTimeFlg;
	@Transient
	private int checkoutTimeFlg;
	@Transient
	private int travelTimeFlg;
	@Transient
	private int workingHoursFlg;
	
	@Transient
	private int allocationMsgFlg;
	
	@Transient
	private int fifteenMinuteMsgFlg;
	
	@Transient
	private int noshowMsgFlg;
	
	@Transient
	private int cabDelayMsgFlg;
	
	@Transient
	private int reachedMsgFlg;
	
	@Transient
	private int hostMobileNumberFlg;
	
	@Transient
	private int employeeIdFlg;
	
	@Transient
	private int employeeNameFlg;
	
	@Transient
	private int empLoacationFlg;	
	
	@Transient
	private int emailIdFlg;
	
	@Transient
	private int employeeMobileNoFlg;
	
	@Transient
	private int selectedCounts;
	
	@Transient
	private int driverAddressFlg;
	
	@Transient
	private int escortIdFlg;
	
	@Transient
	private int escortNameFlg;
	
	@Transient
	private int escortMobileNoFlg;
	
	@Transient
	private int escortAddressFlg;
	
	@Transient
	private String approvalFlg;
	
	@Transient
	private int projectId;

	@Transient
	private int userId;	
	
	@Transient
	private String combinedFacility;
	
	@Column(name="TripStatus", length=10)
	private String tripStatus;
	
	
	@Column(name="BucketStatus", length=10)
	private String bucketStatus;

	@Column(name="TripType", length=10)
	private String tripType;
	
	@Column(name="ShiftTime", length=30)
	private Time shiftTime;
	
	@Column(name="DrivingHours", length=30)
	private Time drivingHours;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TripStartTime", length=30)
	private Date tripStartTime;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TripUpdateTime", length=30)
	private Date tripUpdateTime;

	//bi-directional many-to-one association to eFmFmRouteAreaMapping
	@ManyToOne
	@JoinColumn(name="RouteAreaId")
	private EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping;
	
	//bi-directional many-to-one association to EFmFmEmployeeTripDetail
	@OneToMany(mappedBy="efmFmAssignRoute")
	private List<EFmFmEmployeeTripDetailPO> efmFmEmployeeTripDetails;
	
	//bi-directional many-to-one association to eFmFmActualRouteTravelled
	@OneToMany(mappedBy="efmFmAssignRoute")
	private List<EFmFmActualRoutTravelledPO> eFmFmActualRouteTravelled;
	
	//bi-directional many-to-one association to eFmFmLiveRoutTravelledPO
	@OneToMany(mappedBy="efmFmAssignRoute")
	private List<EFmFmLiveRoutTravelledPO> eFmFmLiveRoutTravelledPO;	
	
	//bi-directional many-to-one association to eFmFmTripAlerts
	@OneToMany(mappedBy="efmFmAssignRoute")
	private List<EFmFmTripAlertsPO> eFmFmTripAlerts;

		
	
	//bi-directional many-to-one association to EFmFmVehicleCheckIn
	@ManyToOne
	@JoinColumn(name="CheckInId")
	private EFmFmVehicleCheckInPO efmFmVehicleCheckIn;
	
	
	//bi-directional many-to-one association to EFmFmVehicleCheckIn
	@ManyToOne
	@JoinColumn(name="EscortCheckInId")
	private EFmFmEscortCheckInPO eFmFmEscortCheckIn;

	
	//bi-directional many-to-one association to EFmFmClientMaster
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;
	
	@Transient
	@JsonProperty("tripSheetValues")
	private List<EFmFmAssignRoutePO> tripValues;
	
	public EFmFmAssignRoutePO(int assignRouteId, String odometerEndKm, String odometerStartKm) {
		super();
		this.assignRouteId = assignRouteId;
		this.odometerEndKm = odometerEndKm;
		this.odometerStartKm = odometerStartKm;
	}	

	

	public EFmFmAssignRoutePO() {
	}
	
	public int getAssignDateFlg() {
		return assignDateFlg;
	}
	public void setAssignDateFlg(int assignDateFlg) {
		this.assignDateFlg = assignDateFlg;
	}


	public String getDistanceUpdationFlg() {
		return distanceUpdationFlg;
	}

	public void setDistanceUpdationFlg(String distanceUpdationFlg) {
		this.distanceUpdationFlg = distanceUpdationFlg;
	}

	public int getRouteCloseTimeFlg() {
		return routeCloseTimeFlg;
	}





	public void setRouteCloseTimeFlg(int routeCloseTimeFlg) {
		this.routeCloseTimeFlg = routeCloseTimeFlg;
	}





	public int getTripStartTimeFlg() {
		return tripStartTimeFlg;
	}





	public void setTripStartTimeFlg(int tripStartTimeFlg) {
		this.tripStartTimeFlg = tripStartTimeFlg;
	}





	public int getCabReachedTimeFlg() {
		return cabReachedTimeFlg;
	}





	public void setCabReachedTimeFlg(int cabReachedTimeFlg) {
		this.cabReachedTimeFlg = cabReachedTimeFlg;
	}





	public int getBoardingStatusFlg() {
		return boardingStatusFlg;
	}





	public void setBoardingStatusFlg(int boardingStatusFlg) {
		this.boardingStatusFlg = boardingStatusFlg;
	}





	public int getBoardingTimeFlg() {
		return boardingTimeFlg;
	}





	public void setBoardingTimeFlg(int boardingTimeFlg) {
		this.boardingTimeFlg = boardingTimeFlg;
	}





	public int getTripEndTimeFlg() {
		return tripEndTimeFlg;
	}





	public void setTripEndTimeFlg(int tripEndTimeFlg) {
		this.tripEndTimeFlg = tripEndTimeFlg;
	}





	public int getPickUpTimeFlg() {
		return pickUpTimeFlg;
	}





	public void setPickUpTimeFlg(int pickUpTimeFlg) {
		this.pickUpTimeFlg = pickUpTimeFlg;
	}





	public int getShiftTimeFlg() {
		return shiftTimeFlg;
	}





	public void setShiftTimeFlg(int shiftTimeFlg) {
		this.shiftTimeFlg = shiftTimeFlg;
	}





	public int getTripTypeFlg() {
		return tripTypeFlg;
	}





	public void setTripTypeFlg(int tripTypeFlg) {
		this.tripTypeFlg = tripTypeFlg;
	}





	public int getDriverNameFlg() {
		return driverNameFlg;
	}





	public void setDriverNameFlg(int driverNameFlg) {
		this.driverNameFlg = driverNameFlg;
	}





	public int getVehicleNumberFlg() {
		return vehicleNumberFlg;
	}





	public void setVehicleNumberFlg(int vehicleNumberFlg) {
		this.vehicleNumberFlg = vehicleNumberFlg;
	}





	public int getRouteNameFlg() {
		return routeNameFlg;
	}





	public void setRouteNameFlg(int routeNameFlg) {
		this.routeNameFlg = routeNameFlg;
	}








	

	

	
	
	public String getCreatedBy() {
        return createdBy;
    }





    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }





    public String getApprovedBy() {
        return approvedBy;
    }





    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }





    public Date getApprovedDate() {
        return approvedDate;
    }





    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }





    public int getAssignRouteId() {
		return assignRouteId;
	}



	public void setAssignRouteId(int assignRouteId) {
		this.assignRouteId = assignRouteId;
	}



	public double getEditedTravelledDistance() {
        return editedTravelledDistance;
    }



    public void setEditedTravelledDistance(double editedTravelledDistance) {
        this.editedTravelledDistance = editedTravelledDistance;
    }



    public String getVehicleStatus() {
		return vehicleStatus;
	}



	public void setVehicleStatus(String vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}

	public String getToDate() {
		return toDate;
	}


	public void setToDate(String toDate) {
		this.toDate = toDate;
	}


	

	public int getRequestId() {
		return requestId;
	}



	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}



	public String getFromDate() {
		return fromDate;
	}



	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}


	public String getTime() {
		return time;
	}



	public void setTime(String time) {
		this.time = time;
	}



	public String getAllocationMsg() {
		return allocationMsg;
	}



	public void setAllocationMsg(String allocationMsg) {
		this.allocationMsg = allocationMsg;
	}



	public String getEscortRequredFlag() {
		return this.escortRequredFlag;
	}

	public void setEscortRequredFlag(String escortRequredFlag) {
		this.escortRequredFlag = escortRequredFlag;
	}

	public String getOdometerEndKm() {
		return this.odometerEndKm;
	}

	public void setOdometerEndKm(String odometerEndKm) {
		this.odometerEndKm = odometerEndKm;
	}

	public String getOdometerStartKm() {
		return this.odometerStartKm;
	}

	public void setOdometerStartKm(String odometerStartKm) {
		this.odometerStartKm = odometerStartKm;
	}

	public double getPlannedDistance() {
		return this.plannedDistance;
	}

	public void setPlannedDistance(double plannedDistance) {
		this.plannedDistance = plannedDistance;
	}

	public String getRouteGenerationType() {
		return this.routeGenerationType;
	}

	public void setRouteGenerationType(String routeGenerationType) {
		this.routeGenerationType = routeGenerationType;
	}

	public double getTravelledDistance() {
		return this.travelledDistance;
	}

	public void setTravelledDistance(double travelledDistance) {
		this.travelledDistance = travelledDistance;
	}

	public Date getTripCompleteTime() {
		return this.tripCompleteTime;
	}

	public void setTripCompleteTime(Date tripCompleteTime) {
		this.tripCompleteTime = tripCompleteTime;
	}
	

	public Date getTripAssignDate() {
		return tripAssignDate;
	}



	public String getAdvanceFlag() {
		return advanceFlag;
	}





	public void setAdvanceFlag(String advanceFlag) {
		this.advanceFlag = advanceFlag;
	}





	public int getNewCheckInId() {
		return newCheckInId;
	}



	public void setNewCheckInId(int newCheckInId) {
		this.newCheckInId = newCheckInId;
	}



	public Date getTripStartTime() {
		return tripStartTime;
	}



	public int getVehicleId() {
		return vehicleId;
	}



	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}



	public int getDriverId() {
		return driverId;
	}



	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}



	public String getSearchType() {
		return searchType;
	}



	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}



	public String getEmployeeId() {
		return employeeId;
	}



	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}



	public int getDeviceId() {
		return deviceId;
	}



	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}


	public int getEscortCheckInId() {
		return escortCheckInId;
	}


	public String getEditDistanceApproval() {
        return editDistanceApproval;
    }


    public void setEditDistanceApproval(String editDistanceApproval) {
        this.editDistanceApproval = editDistanceApproval;
    }


    public void setEscortCheckInId(int escortCheckInId) {
		this.escortCheckInId = escortCheckInId;
	}


	public int getSelectedAssignRouteId() {
		return selectedAssignRouteId;
	}



	public void setSelectedAssignRouteId(int selectedAssignRouteId) {
		this.selectedAssignRouteId = selectedAssignRouteId;
	}



	public void setTripStartTime(Date tripStartTime) {
		this.tripStartTime = tripStartTime;
	}



	public void setTripAssignDate(Date tripAssignDate) {
		this.tripAssignDate = tripAssignDate;
	}


	public String getTripStatus() {
		return this.tripStatus;
	}

	public void setTripStatus(String tripStatus) {
		this.tripStatus = tripStatus;
	}

	public String getTripType() {
		return this.tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}


	public EFmFmRouteAreaMappingPO geteFmFmRouteAreaMapping() {
		return eFmFmRouteAreaMapping;
	}

	public void seteFmFmRouteAreaMapping(
			EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping) {
		this.eFmFmRouteAreaMapping = eFmFmRouteAreaMapping;
	}

	public EFmFmVehicleCheckInPO getEfmFmVehicleCheckIn() {
		return this.efmFmVehicleCheckIn;
	}

	public void setEfmFmVehicleCheckIn(EFmFmVehicleCheckInPO efmFmVehicleCheckIn) {
		this.efmFmVehicleCheckIn = efmFmVehicleCheckIn;
	}

	public List<EFmFmEmployeeTripDetailPO> getEfmFmEmployeeTripDetails() {
		return efmFmEmployeeTripDetails;
	}

	public void setEfmFmEmployeeTripDetails(
			List<EFmFmEmployeeTripDetailPO> efmFmEmployeeTripDetails) {
		this.efmFmEmployeeTripDetails = efmFmEmployeeTripDetails;
	}

	

	public String getRemarksForEditingTravelledDistance() {
        return remarksForEditingTravelledDistance;
    }



    public void setRemarksForEditingTravelledDistance(String remarksForEditingTravelledDistance) {
        this.remarksForEditingTravelledDistance = remarksForEditingTravelledDistance;
    }



    public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}



	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}



	public List<EFmFmActualRoutTravelledPO> geteFmFmActualRouteTravelled() {
		return eFmFmActualRouteTravelled;
	}

	public void seteFmFmActualRouteTravelled(
			List<EFmFmActualRoutTravelledPO> eFmFmActualRouteTravelled) {
		this.eFmFmActualRouteTravelled = eFmFmActualRouteTravelled;
	}

	public List<EFmFmTripAlertsPO> geteFmFmTripAlerts() {
		return eFmFmTripAlerts;
	}

	public void seteFmFmTripAlerts(List<EFmFmTripAlertsPO> eFmFmTripAlerts) {
		this.eFmFmTripAlerts = eFmFmTripAlerts;
	}

	public Time getShiftTime() {
		return shiftTime;
	}



	public void setShiftTime(Time shiftTime) {
		this.shiftTime = shiftTime;
	}



	public EFmFmEscortCheckInPO geteFmFmEscortCheckIn() {
		return eFmFmEscortCheckIn;
	}

	public String getBucketStatus() {
		return bucketStatus;
	}



	public void setBucketStatus(String bucketStatus) {
		this.bucketStatus = bucketStatus;
	}



	public void seteFmFmEscortCheckIn(EFmFmEscortCheckInPO eFmFmEscortCheckIn) {
		this.eFmFmEscortCheckIn = eFmFmEscortCheckIn;
	}



	public Date getAllocationMsgDeliveryDate() {
		return allocationMsgDeliveryDate;
	}



	public void setAllocationMsgDeliveryDate(Date allocationMsgDeliveryDate) {
		this.allocationMsgDeliveryDate = allocationMsgDeliveryDate;
	}



	public int getVendorId() {
		return vendorId;
	}



	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}



	public int getTripId() {
		return tripId;
	}



	public void setTripId(int tripId) {
		this.tripId = tripId;
	}



	public String getBoardedFlg() {
		return boardedFlg;
	}



	public void setBoardedFlg(String boardedFlg) {
		this.boardedFlg = boardedFlg;
	}



	public String getNodalPoints() {
		return nodalPoints;
	}



	public void setNodalPoints(String nodalPoints) {
		this.nodalPoints = nodalPoints;
	}



	public int getNodalPointId() {
		return nodalPointId;
	}



	public void setNodalPointId(int nodalPointId) {
		this.nodalPointId = nodalPointId;
	}



	public Date getCreatedDate() {
		return createdDate;
	}



	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}



    public int getBranchId() {
        return branchId;
    }



    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
    public Date getTripUpdateTime() {
        return tripUpdateTime;
    }



    public void setTripUpdateTime(Date tripUpdateTime) {
        this.tripUpdateTime = tripUpdateTime;
    }


	public double getFuelKmAmount() {
		return fuelKmAmount;
	}

	public void setFuelKmAmount(double fuelKmAmount) {
		this.fuelKmAmount = fuelKmAmount;
	}


	public long getPlannedTime() {
		return plannedTime;
	}

	public void setPlannedTime(long plannedTime) {
		this.plannedTime = plannedTime;
	}





	public String getPlannedReadFlg() {
		return plannedReadFlg;
	}





	public void setPlannedReadFlg(String plannedReadFlg) {
		this.plannedReadFlg = plannedReadFlg;
	}





	public String getScheduleReadFlg() {
		return scheduleReadFlg;
	}





	public void setScheduleReadFlg(String scheduleReadFlg) {
		this.scheduleReadFlg = scheduleReadFlg;
	}





	public double getPlannedTravelledDistance() {
		return plannedTravelledDistance;
	}





	public void setPlannedTravelledDistance(double plannedTravelledDistance) {
		this.plannedTravelledDistance = plannedTravelledDistance;
	}





	public String getRequestType() {
		return requestType;
	}





	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public List<EFmFmLiveRoutTravelledPO> geteFmFmLiveRoutTravelledPO() {
		return eFmFmLiveRoutTravelledPO;
	}

	public void seteFmFmLiveRoutTravelledPO(List<EFmFmLiveRoutTravelledPO> eFmFmLiveRoutTravelledPO) {
		this.eFmFmLiveRoutTravelledPO = eFmFmLiveRoutTravelledPO;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public Time getDrivingHours() {
		return drivingHours;
	}


	public void setDrivingHours(Time drivingHours) {
		this.drivingHours = drivingHours;
	}

	public String getRouteName() {
		return routeName;
	}


	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public int getEmployeeIdFlg() {
		return employeeIdFlg;
	}
	public void setEmployeeIdFlg(int employeeIdFlg) {
		this.employeeIdFlg = employeeIdFlg;
	}
	public int getCheckInTimeFlg() {
		return checkInTimeFlg;
	}
	public void setCheckInTimeFlg(int checkInTimeFlg) {
		this.checkInTimeFlg = checkInTimeFlg;
	}
	public int getCheckoutTimeFlg() {
		return checkoutTimeFlg;
	}
	public void setCheckoutTimeFlg(int checkoutTimeFlg) {
		this.checkoutTimeFlg = checkoutTimeFlg;
	}
	public int getTravelTimeFlg() {
		return travelTimeFlg;
	}
	public void setTravelTimeFlg(int travelTimeFlg) {
		this.travelTimeFlg = travelTimeFlg;
	}
	public int getWorkingHoursFlg() {
		return workingHoursFlg;
	}
	public void setWorkingHoursFlg(int workingHoursFlg) {
		this.workingHoursFlg = workingHoursFlg;
	}
	public int getAllocationMsgFlg() {
		return allocationMsgFlg;
	}
	public void setAllocationMsgFlg(int allocationMsgFlg) {
		this.allocationMsgFlg = allocationMsgFlg;
	}
	public int getCabDelayMsgFlg() {
		return cabDelayMsgFlg;
	}
	public void setCabDelayMsgFlg(int cabDelayMsgFlg) {
		this.cabDelayMsgFlg = cabDelayMsgFlg;
	}
	public int getReachedMsgFlg() {
		return reachedMsgFlg;
	}
	public void setReachedMsgFlg(int reachedMsgFlg) {
		this.reachedMsgFlg = reachedMsgFlg;
	}
	public int getNoshowMsgFlg() {
		return noshowMsgFlg;
	}
	public void setNoshowMsgFlg(int noshowMsgFlg) {
		this.noshowMsgFlg = noshowMsgFlg;
	}
	public int getFifteenMinuteMsgFlg() {
		return fifteenMinuteMsgFlg;
	}
	public void setFifteenMinuteMsgFlg(int fifteenMinuteMsgFlg) {
		this.fifteenMinuteMsgFlg = fifteenMinuteMsgFlg;
	}
	public int getHostMobileNumberFlg() {
		return hostMobileNumberFlg;
	}
	public void setHostMobileNumberFlg(int hostMobileNumberFlg) {
		this.hostMobileNumberFlg = hostMobileNumberFlg;
	}
	public int getEmpLoacationFlg() {
		return empLoacationFlg;
	}
	public void setEmpLoacationFlg(int empLoacationFlg) {
		this.empLoacationFlg = empLoacationFlg;
	}
	public int getEmailIdFlg() {
		return emailIdFlg;
	}
	public void setEmailIdFlg(int emailIdFlg) {
		this.emailIdFlg = emailIdFlg;
	}

	public int getEmployeeNameFlg() {
		return employeeNameFlg;
	}
	public void setEmployeeNameFlg(int employeeNameFlg) {
		this.employeeNameFlg = employeeNameFlg;
	}
	public int getRouteIdFlg() {
		return routeIdFlg;
	}
	public void setRouteIdFlg(int routeIdFlg) {
		this.routeIdFlg = routeIdFlg;
	}
	public int getDriverIdFlg() {
		return driverIdFlg;
	}
	public void setDriverIdFlg(int driverIdFlg) {
		this.driverIdFlg = driverIdFlg;
	}
	
	public int getRemarksFlg() {
		return remarksFlg;
	}
	public void setRemarksFlg(int remarksFlg) {
		this.remarksFlg = remarksFlg;
	}

	
	
	public int getEscortFlg() {
		return escortFlg;
	}
	public void setEscortFlg(int escortFlg) {
		this.escortFlg = escortFlg;
	}
	
	public int getPlannedDistanceFlg() {
		return plannedDistanceFlg;
	}
	public void setPlannedDistanceFlg(int plannedDistanceFlg) {
		this.plannedDistanceFlg = plannedDistanceFlg;
	}
	public int getGpsFlg() {
		return gpsFlg;
	}
	public void setGpsFlg(int gpsFlg) {
		this.gpsFlg = gpsFlg;
	}
	public int getSelectedCounts() {
		return selectedCounts;
	}
	public void setSelectedCounts(int selectedCounts) {
		this.selectedCounts = selectedCounts;
	}
	public int getDriverMobileNumberFlg() {
		return driverMobileNumberFlg;
	}
	public void setDriverMobileNumberFlg(int driverMobileNumberFlg) {
		this.driverMobileNumberFlg = driverMobileNumberFlg;
	}
	public int getDriverAddressFlg() {
		return driverAddressFlg;
	}
	public void setDriverAddressFlg(int driverAddressFlg) {
		this.driverAddressFlg = driverAddressFlg;
	}
	public int getEscortIdFlg() {
		return escortIdFlg;
	}
	public void setEscortIdFlg(int escortIdFlg) {
		this.escortIdFlg = escortIdFlg;
	}
	public int getEscortNameFlg() {
		return escortNameFlg;
	}
	public void setEscortNameFlg(int escortNameFlg) {
		this.escortNameFlg = escortNameFlg;
	}
	public int getEscortMobileNoFlg() {
		return escortMobileNoFlg;
	}
	public void setEscortMobileNoFlg(int escortMobileNoFlg) {
		this.escortMobileNoFlg = escortMobileNoFlg;
	}
	public int getEscortAddressFlg() {
		return escortAddressFlg;
	}
	public void setEscortAddressFlg(int escortAddressFlg) {
		this.escortAddressFlg = escortAddressFlg;
	}
	public int getEmployeeMobileNoFlg() {
		return employeeMobileNoFlg;
	}
	public void setEmployeeMobileNoFlg(int employeeMobileNoFlg) {
		this.employeeMobileNoFlg = employeeMobileNoFlg;
	}
	public String getVendorIdSelected() {
		return vendorIdSelected;
	}
	public void setVendorIdSelected(String vendorIdSelected) {
		this.vendorIdSelected = vendorIdSelected;
	}
	public int getVendorNameFlg() {
		return vendorNameFlg;
	}
	public void setVendorNameFlg(int vendorNameFlg) {
		this.vendorNameFlg = vendorNameFlg;
	}
	public int getTotalWorkingHoursFlg() {
		return totalWorkingHoursFlg;
	}
	public void setTotalWorkingHoursFlg(int totalWorkingHoursFlg) {
		this.totalWorkingHoursFlg = totalWorkingHoursFlg;
	}
	public int getDriverDrivingHoursPerTripFlg() {
		return driverDrivingHoursPerTripFlg;
	}
	public void setDriverDrivingHoursPerTripFlg(int driverDrivingHoursPerTripFlg) {
		this.driverDrivingHoursPerTripFlg = driverDrivingHoursPerTripFlg;
	}
	public int getTotalDrivingHoursFlg() {
		return totalDrivingHoursFlg;
	}
	public void setTotalDrivingHoursFlg(int totalDrivingHoursFlg) {
		this.totalDrivingHoursFlg = totalDrivingHoursFlg;
	}
	public List<EFmFmAssignRoutePO> getTripValues() {
		return tripValues;
	}
	public void setTripValues(List<EFmFmAssignRoutePO> tripValues) {
		this.tripValues = tripValues;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public String getIsBackTwoBack() {
		return isBackTwoBack;
	}

	public void setIsBackTwoBack(String isBackTwoBack) {
		this.isBackTwoBack = isBackTwoBack;
	}

	public int getBackTwoBackRouteId() {
		return backTwoBackRouteId;
	}

	public void setBackTwoBackRouteId(int backTwoBackRouteId) {
		this.backTwoBackRouteId = backTwoBackRouteId;
	}



	public String getAssignedVendorName() {
		return assignedVendorName;
	}



	public void setAssignedVendorName(String assignedVendorName) {
		this.assignedVendorName = assignedVendorName;
	}


	public String getRoutingCompletionStatus() {
		return routingCompletionStatus;
	}

	public void setRoutingCompletionStatus(String routingCompletionStatus) {
		this.routingCompletionStatus = routingCompletionStatus;
	}

	public String getBulkRouteIds() {
		return bulkRouteIds;
	}

	public void setBulkRouteIds(String bulkRouteIds) {
		this.bulkRouteIds = bulkRouteIds;
	}

	public String getBulkCheckInIds() {
		return bulkCheckInIds;
	}

	public void setBulkCheckInIds(String bulkCheckInIds) {
		this.bulkCheckInIds = bulkCheckInIds;
	}



	public String getIsToll() {
		return isToll;
	}



	public void setIsToll(String isToll) {
		this.isToll = isToll;
	}



	public String getLocationFlg() {
		return locationFlg;
	}



	public void setLocationFlg(String locationFlg) {
		this.locationFlg = locationFlg;
	}



	public String getEditDistanceType() {
		return editDistanceType;
	}



	public void setEditDistanceType(String editDistanceType) {
		this.editDistanceType = editDistanceType;
	}



	public String getApprovalFlg() {
		return approvalFlg;
	}



	public void setApprovalFlg(String approvalFlg) {
		this.approvalFlg = approvalFlg;
	}



	public String getTripConfirmationFromDriver() {
		return tripConfirmationFromDriver;
	}



	public void setTripConfirmationFromDriver(String tripConfirmationFromDriver) {
		this.tripConfirmationFromDriver = tripConfirmationFromDriver;
	}



	public String getReasonForDelay() {
		return reasonForDelay;
	}



	public void setReasonForDelay(String reasonForDelay) {
		this.reasonForDelay = reasonForDelay;
	}



	public int getProjectId() {
		return projectId;
	}



	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}



	public String getTripSheetModifiedStatus() {
		return tripSheetModifiedStatus;
	}



	public void setTripSheetModifiedStatus(String tripSheetModifiedStatus) {
		this.tripSheetModifiedStatus = tripSheetModifiedStatus;
	}



	public int getSelectedBaseFacility() {
		return selectedBaseFacility;
	}



	public void setSelectedBaseFacility(int selectedBaseFacility) {
		this.selectedBaseFacility = selectedBaseFacility;
	}



	public String getCombinedFacility() {
		return combinedFacility;
	}



	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}
	
	
	
}