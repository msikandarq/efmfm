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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * The persistent class for the eFmFmEmployeeRequestMaster database table.
 * 
 */
@Entity
@Table(name="eFmFmEmployeeRequestMaster")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EFmFmEmployeeRequestMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TripId", length=50)
	private int tripId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RequestDate", length=30)
	private Date requestDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TripRequestEndDate", length=30)
	private Date tripRequestEndDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TripRequestStartDate", length=30)
	private Date tripRequestStartDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ActualTripEndDate", length=30)
	private Date actulaTripEndDate;

	@Column(name="DropSequence", length=50)
	private int dropSequence;

	@Column(name="ShiftTime", length=30)
	private Time shiftTime;
	
	@Column(name="PICKUP_TIME")
	private Time pickUpTime;
	
    @Column(name="waitingTime")
	private long  waitingTime;


	@Column(name="TripType", length=10)
	private String tripType;
	
	@Column(name="Status", length=10)
	private String status;
	
	@Column(name="ReadFlg", length=10)
	private String readFlg;
	
	@Column(name="RequestFrom", length=10)
	private String requestFrom;
	
	@Column(name="RequestType", length=30)
	private String requestType;
	
	
	@Column(name="BookedBy", length=200)
    private String bookedBy;
		
	@Column(name="ChargeTo", length=200)
    private String chargedTo;
	
	@Column(name="AccountName", length=200)
    private String accountName;
	
	@Column(name="OriginAddress", length=250)
    private String originAddress;
	
	@Column(name="OriginAddressLattitudeLongitude", length=250)
    private String originLattitudeLongitude;
	
	@Column(name="DestinationAddress", length=250)
    private String endDestinationAddress;
    
	@Column(name="DestinationAddressLattitudeLongitude", length=250)
    private String endDestinationAddressLattitudeLongitude;
	
	@Column(name="Destination1Address", length=250)
    private String destination1Address;
	
	@Column(name="Destination1AddressLattitudeLongitude", length=250)
    private String destination1AddressLattitudeLongitude;
	
	@Column(name="Destination2Address", length=250)
    private String destination2Address;
	
	@Column(name="Destination2AddressLattitudeLongitude", length=250)
    private String destination2AddressLattitudeLongitude;
	
	@Column(name="Destination3Address", length=250)
    private String destination3Address;
	
	@Column(name="Destination3AddressLattitudeLongitude", length=250)
    private String destination3AddressLattitudeLongitude;
	
	@Column(name="Destination4Address", length=250)
    private String destination4Address;
	
	@Column(name="Destination4AddressLattitudeLongitude", length=250)
    private String destination4AddressLattitudeLongitude;
	
	@Column(name="Destination5Address", length=250)
    private String destination5Address;
	
	@Column(name="Destination5AddressLattitudeLongitude", length=250)
    private String destination5AddressLattitudeLongitude;
	
	@Column(name="ReservationType", length=200)
    private String reservationType;
	
	@Column(name="DurationInHours", length=200)
    private int durationInHours;
	
	@Column(name="PaymentType", length=200)
    private String paymentType;
	
	@Column(name="Remarks", length=250)
    private String remarks;
	/*
	 * Single-S(origin,one destination  from 
	 * drop Down,M-MultipleDestination,N-Normal Request
	 */
	@Column(name="LocationFlg", length=250)
    private String locationFlg;
	
	@Column(name="LocationWaypointsIds", length=500)
    private String locationWaypointsIds;	
	
	@Column(name = "ReportingManagerUserId", length = 100)
	private String reportingManagerUserId;
	
	@Column(name="ReqApprovalStatus", length=500)
    private String reqApprovalStatus;
	/*
	 * ShiftTime-S, Adhoc Time =A
	 */
	@Column(name="TimeFlg", length=10)
	private String timeFlg;
	
	@Column(name="ProjectId", length=500)
    private int projectId;
	
	@Column(name="BranchName", length=150)
    private String branchName;
	

	@Transient
	private String cutOffTimeFlg;
	
	@Transient
	private String time;
	
	@Transient
	private String firstName;
	
	@Transient
	private String lastName;
	
	@Transient
	private String emailId;

	@Transient
	private String mobileNumber;
	
	@Transient
	private String latitudeLongitude;
	
	@Transient
	private String address;
	
	@Transient
	private String gender;	
	
	@Transient
	private int userId;
	
	
	@Transient
	private int branchId;
	
	
	@Transient
	private String routeName;
	
	@Transient
	private String areaName;
	
	
	@Transient
	private String employeeId;

	
	@Transient
	private String startDate;
	
	@Transient
	private String role;
	
	@Transient
	private int shiftId;
	
	@Transient
	private int pickupShiftId;
	
	@Transient
	private int dropShiftId;

	@Transient
	private String endDate;
	@Transient
	private String hostMobileNumber;
	@Transient
	private String guestMiddleLoc;
	
	@Transient
	private int routeAreaId;
	
	@Transient
	private String projectName;
	
	@Transient
	private int multipleEmpIds;
	
	@Transient
	private String multipleProjectEmpIds;
	
	@Transient
	private int multipleRequestFlg;
	
	@Transient
	private String pickupTime;
	
	@Transient
	private String dropTime;
	
	@Transient
	private int startPickupLocation;
	
	@Transient
	private int startDropLocation;
	
	
	@Transient
	private int endPickupLocation;
	
	@Transient
	private int endDropLocation;
	
	@Transient
	private String combinedFacility;
	
	@Transient
	@JsonProperty("tripRequestValues")
	private List<EFmFmEmployeeRequestMasterPO> tripRequests;
	
	
	
			
	//bidirectional @OneToMany association to eFmFmEmployeeTravelRequest
	@OneToMany(mappedBy="eFmFmEmployeeRequestMaster")
	private List<EFmFmEmployeeTravelRequestPO> eFmFmEmployeeTravelRequest;
		
	
	//bi-directional many-to-one association to EFmFmUserMaster
	@ManyToOne
	@JoinColumn(name="UserId")
	private EFmFmUserMasterPO efmFmUserMaster;
	
	
	// bi-directional many-to-one association to EFmFmClientBranchPO
		@ManyToOne
		@JoinColumn(name = "BranchId")
		private EFmFmClientBranchPO eFmFmClientBranchPO;

	
	
	//bi-directional many-to-one association to EFmFmAreaMaster
	@ManyToOne
	@JoinColumn(name="RouteAreaId")
	private EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping;

	
	public EFmFmEmployeeRequestMasterPO() {
	}

	

	public int getTripId() {
		return tripId;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public void setTripId(int tripId) {
		this.tripId = tripId;
	}
 
	public String getReadFlg() {
		return readFlg;
	}

	
	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}



	public Time getPickUpTime() {
		return pickUpTime;
	}



	public void setPickUpTime(Time pickUpTime) {
		this.pickUpTime = pickUpTime;
	}



	public String getEndDate() {
		return endDate;
	}



	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}



	public void setReadFlg(String readFlg) {
		this.readFlg = readFlg;
	}


	
	public String getRequestType() {
		return requestType;
	}



	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}


	public int getShiftId() {
        return shiftId;
    }



    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }



    public String getRequestFrom() {
		return requestFrom;
	}


	public void setRequestFrom(String requestFrom) {
		this.requestFrom = requestFrom;
	}
	
	public Time getShiftTime() {
		return shiftTime;
	}



	public void setShiftTime(Time shiftTime) {
		this.shiftTime = shiftTime;
	}



	public int getUserId() {
		return userId;
	}



	public void setUserId(int userId) {
		this.userId = userId;
	}



	public Date getActulaTripEndDate() {
		return actulaTripEndDate;
	}

	public void setActulaTripEndDate(Date actulaTripEndDate) {
		this.actulaTripEndDate = actulaTripEndDate;
	}

	
	public long getWaitingTime() {
		return waitingTime;
	}



	public void setWaitingTime(long waitingTime) {
		this.waitingTime = waitingTime;
	}



	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	
	
	public String getEmployeeId() {
		return employeeId;
	}



	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}



	public Date getRequestDate() {
		return this.requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getTripRequestEndDate() {
		return this.tripRequestEndDate;
	}

	public void setTripRequestEndDate(Date tripRequestEndDate) {
		this.tripRequestEndDate = tripRequestEndDate;
	}

	public Date getTripRequestStartDate() {
		return this.tripRequestStartDate;
	}

	public void setTripRequestStartDate(Date tripRequestStartDate) {
		this.tripRequestStartDate = tripRequestStartDate;
	}

	
	
	public String getTripType() {
		return this.tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}


	public List<EFmFmEmployeeTravelRequestPO> geteFmFmEmployeeTravelRequest() {
		return eFmFmEmployeeTravelRequest;
	}



	public void seteFmFmEmployeeTravelRequest(
			List<EFmFmEmployeeTravelRequestPO> eFmFmEmployeeTravelRequest) {
		this.eFmFmEmployeeTravelRequest = eFmFmEmployeeTravelRequest;
	}



	public EFmFmUserMasterPO getEfmFmUserMaster() {
		return efmFmUserMaster;
	}



	public void setEfmFmUserMaster(EFmFmUserMasterPO efmFmUserMaster) {
		this.efmFmUserMaster = efmFmUserMaster;
	}



	public String getRole() {
		return role;
	}



	public void setRole(String role) {
		this.role = role;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getEmailId() {
		return emailId;
	}



	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}



	public String getMobileNumber() {
		return mobileNumber;
	}



	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}



	public String getLatitudeLongitude() {
		return latitudeLongitude;
	}



	public void setLatitudeLongitude(String latitudeLongitude) {
		this.latitudeLongitude = latitudeLongitude;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getGender() {
		return gender;
	}



	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHostMobileNumber() {
		return hostMobileNumber;
	}



	public void setHostMobileNumber(String hostMobileNumber) {
		this.hostMobileNumber = hostMobileNumber;
	}



	public String getGuestMiddleLoc() {
		return guestMiddleLoc;
	}



	public void setGuestMiddleLoc(String guestMiddleLoc) {
		this.guestMiddleLoc = guestMiddleLoc;
	}



    public int getDropSequence() {
        return dropSequence;
    }



    public void setDropSequence(int dropSequence) {
        this.dropSequence = dropSequence;
    }



    public String getCutOffTimeFlg() {
        return cutOffTimeFlg;
    }



    public void setCutOffTimeFlg(String cutOffTimeFlg) {
        this.cutOffTimeFlg = cutOffTimeFlg;
    }



    public String getBookedBy() {
        return bookedBy;
    }



    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }



    public String getChargedTo() {
        return chargedTo;
    }



    public void setChargedTo(String chargedTo) {
        this.chargedTo = chargedTo;
    }



    public String getAccountName() {
        return accountName;
    }



    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }



    public String getOriginAddress() {
        return originAddress;
    }



    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }



    public String getOriginLattitudeLongitude() {
        return originLattitudeLongitude;
    }



    public void setOriginLattitudeLongitude(String originLattitudeLongitude) {
        this.originLattitudeLongitude = originLattitudeLongitude;
    }



    public String getEndDestinationAddress() {
        return endDestinationAddress;
    }



    public void setEndDestinationAddress(String endDestinationAddress) {
        this.endDestinationAddress = endDestinationAddress;
    }



    public String getEndDestinationAddressLattitudeLongitude() {
        return endDestinationAddressLattitudeLongitude;
    }



    public void setEndDestinationAddressLattitudeLongitude(String endDestinationAddressLattitudeLongitude) {
        this.endDestinationAddressLattitudeLongitude = endDestinationAddressLattitudeLongitude;
    }



    public String getDestination1Address() {
        return destination1Address;
    }



    public void setDestination1Address(String destination1Address) {
        this.destination1Address = destination1Address;
    }



    public String getDestination1AddressLattitudeLongitude() {
        return destination1AddressLattitudeLongitude;
    }



    public void setDestination1AddressLattitudeLongitude(String destination1AddressLattitudeLongitude) {
        this.destination1AddressLattitudeLongitude = destination1AddressLattitudeLongitude;
    }



    public String getDestination2Address() {
        return destination2Address;
    }



    public void setDestination2Address(String destination2Address) {
        this.destination2Address = destination2Address;
    }



    public String getDestination2AddressLattitudeLongitude() {
        return destination2AddressLattitudeLongitude;
    }



    public void setDestination2AddressLattitudeLongitude(String destination2AddressLattitudeLongitude) {
        this.destination2AddressLattitudeLongitude = destination2AddressLattitudeLongitude;
    }



    public String getDestination3Address() {
        return destination3Address;
    }



    public void setDestination3Address(String destination3Address) {
        this.destination3Address = destination3Address;
    }



    public String getDestination3AddressLattitudeLongitude() {
        return destination3AddressLattitudeLongitude;
    }



    public void setDestination3AddressLattitudeLongitude(String destination3AddressLattitudeLongitude) {
        this.destination3AddressLattitudeLongitude = destination3AddressLattitudeLongitude;
    }



    public String getDestination4Address() {
        return destination4Address;
    }



    public void setDestination4Address(String destination4Address) {
        this.destination4Address = destination4Address;
    }



    public String getDestination4AddressLattitudeLongitude() {
        return destination4AddressLattitudeLongitude;
    }



    public void setDestination4AddressLattitudeLongitude(String destination4AddressLattitudeLongitude) {
        this.destination4AddressLattitudeLongitude = destination4AddressLattitudeLongitude;
    }



    public String getDestination5Address() {
        return destination5Address;
    }



    public void setDestination5Address(String destination5Address) {
        this.destination5Address = destination5Address;
    }



    public String getDestination5AddressLattitudeLongitude() {
        return destination5AddressLattitudeLongitude;
    }



    public void setDestination5AddressLattitudeLongitude(String destination5AddressLattitudeLongitude) {
        this.destination5AddressLattitudeLongitude = destination5AddressLattitudeLongitude;
    }



    public String getReservationType() {
        return reservationType;
    }



    public void setReservationType(String reservationType) {
        this.reservationType = reservationType;
    }



    public int getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(int durationInHours) {
        this.durationInHours = durationInHours;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }



	public int getBranchId() {
		return branchId;
	}



	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}



	public String getRouteName() {
		return routeName;
	}



	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}



	public String getAreaName() {
		return areaName;
	}



	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}



	public EFmFmRouteAreaMappingPO geteFmFmRouteAreaMapping() {
		return eFmFmRouteAreaMapping;
	}



	public void seteFmFmRouteAreaMapping(EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping) {
		this.eFmFmRouteAreaMapping = eFmFmRouteAreaMapping;
	}



	public String getLocationFlg() {
		return locationFlg;
	}



	public void setLocationFlg(String locationFlg) {
		this.locationFlg = locationFlg;
	}



	public String getLocationWaypointsIds() {
		return locationWaypointsIds;
	}



	public void setLocationWaypointsIds(String locationWaypointsIds) {
		this.locationWaypointsIds = locationWaypointsIds;
	}


	public String getReqApprovalStatus() {
		return reqApprovalStatus;
	}



	public void setReqApprovalStatus(String reqApprovalStatus) {
		this.reqApprovalStatus = reqApprovalStatus;
	}



	



	public String getReportingManagerUserId() {
		return reportingManagerUserId;
	}



	public void setReportingManagerUserId(String reportingManagerUserId) {
		this.reportingManagerUserId = reportingManagerUserId;
	}



	public int getProjectId() {
		return projectId;
	}



	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}



	public int getRouteAreaId() {
		return routeAreaId;
	}



	public void setRouteAreaId(int routeAreaId) {
		this.routeAreaId = routeAreaId;
	}



	public String getMultipleProjectEmpIds() {
		return multipleProjectEmpIds;
	}



	public void setMultipleProjectEmpIds(String multipleProjectEmpIds) {
		this.multipleProjectEmpIds = multipleProjectEmpIds;
	}



	public int getMultipleEmpIds() {
		return multipleEmpIds;
	}



	public void setMultipleEmpIds(int multipleEmpIds) {
		this.multipleEmpIds = multipleEmpIds;
	}



	public String getTimeFlg() {
		return timeFlg;
	}



	public void setTimeFlg(String timeFlg) {
		this.timeFlg = timeFlg;
	}



	



	public int getMultipleRequestFlg() {
		return multipleRequestFlg;
	}



	public void setMultipleRequestFlg(int multipleRequestFlg) {
		this.multipleRequestFlg = multipleRequestFlg;
	}



	public String getPickupTime() {
		return pickupTime;
	}



	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}



	public String getDropTime() {
		return dropTime;
	}


	public void setDropTime(String dropTime) {
		this.dropTime = dropTime;
	}
	
	public List<EFmFmEmployeeRequestMasterPO> getTripRequests() {
		return tripRequests;
	}



	public void setTripRequests(List<EFmFmEmployeeRequestMasterPO> tripRequests) {
		this.tripRequests = tripRequests;
	}



	



	public int getStartDropLocation() {
		return startDropLocation;
	}



	public void setStartDropLocation(int startDropLocation) {
		this.startDropLocation = startDropLocation;
	}

	
	public int getEndDropLocation() {
		return endDropLocation;
	}

	public void setEndDropLocation(int endDropLocation) {
		this.endDropLocation = endDropLocation;
	}

	public int getStartPickupLocation() {
		return startPickupLocation;
	}



	public void setStartPickupLocation(int startPickupLocation) {
		this.startPickupLocation = startPickupLocation;
	}



	public int getEndPickupLocation() {
		return endPickupLocation;
	}



	public void setEndPickupLocation(int endPickupLocation) {
		this.endPickupLocation = endPickupLocation;
	}



	/**
	 * @param startDate
	 * @param endDate
	 * @param tripType
	 * @param timeFlg
	 * @param pickupTime
	 * @param dropTime
	 * @param startPikcupLocation
	 * @param startDropLocation
	 * @param endPikcupLocation
	 * @param endDropLocation
	 */
	public EFmFmEmployeeRequestMasterPO(String startDate, String endDate, String tripType, String timeFlg,
			 String pickupTime, String dropTime, int startPickupLocation,
			int startDropLocation, int endPickupLocation, int endDropLocation) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.tripType = tripType;
		this.timeFlg = timeFlg;		
		this.pickupTime = pickupTime;
		this.dropTime = dropTime;
		this.startPickupLocation = startPickupLocation;
		this.startDropLocation = startDropLocation;
		this.endPickupLocation = endPickupLocation;
		this.endDropLocation = endDropLocation;
	}



	public String getProjectName() {
		return projectName;
	}



	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}



	public int getPickupShiftId() {
		return pickupShiftId;
	}



	public void setPickupShiftId(int pickupShiftId) {
		this.pickupShiftId = pickupShiftId;
	}



	public int getDropShiftId() {
		return dropShiftId;
	}



	public void setDropShiftId(int dropShiftId) {
		this.dropShiftId = dropShiftId;
	}



	public String getCombinedFacility() {
		return combinedFacility;
	}



	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}



	public String getBranchName() {
		return branchName;
	}



	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}



	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}



	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}  
	
	
   }