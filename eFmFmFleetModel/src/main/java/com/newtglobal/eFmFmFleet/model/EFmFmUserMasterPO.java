package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * The persistent class for the eFmFmUserMaster database table.
 * 
 */
@Entity
@Table(name = "eFmFmUserMaster")
public class EFmFmUserMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "UserId", length = 10)
	private int userId;

	@Column(name = "CreatedBy", length = 50)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreationTime", length = 50)
	private Date creationTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DateOfBirth", length = 50)
	private Date DateOfBirth;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PasswordChangeDate", length = 50)
	private Date passwordChangeDate;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WrongPassAttemptDate", length = 50)
    private Date wrongPassAttemptDate;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LastLoginTime", length = 50)
    private Date lastLoginTime;

	@Lob
	@Column(name = "Address", length = 1000)
	private String address;
	
	
	@Column(name = "GeoCodedAddress", length = 250)
	private String geoCodedAddress;
	
	@Column(name = "HomeGeoCodePoints", length = 250)
	private String homeGeoCodePoints;
	
	

	@Column(name = "LoggedIn", length = 10)
	private boolean loggedIn;
	
	@Column(name = "TempPassWordChange", length = 10)
    private boolean tempPassWordChange;

	@Column(name = "DeviceId", length = 100)
	private String deviceId;

	@Column(name = "DeviceToken", length = 250)
	private String deviceToken;

	@Column(name = "EmployeeDesignation", length = 200)
	private String employeeDesignation;
	
	@Column(name = "OHRId", length = 200)
	private String OHRId;
	
	@Column(name = "TripType", length = 50)
	private String tripType;
	
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "OTPGenratingDate", length = 50)
    private Date oTPGenratingDate;
 
 
    @Column(name = "OTPGenratingCount", length = 50)
    private int oTPGenratingCount=0;

	@Transient
	private String newPassword;
	
	@Transient
    private String areaName;	
	
	@Transient
    private String searchType;

	@Transient
	private String birthDate;

	@Column(name = "DeviceType", length = 50)
	private String deviceType;

	@Column(name = "LocationStatus", length = 50)
	private String locationStatus;

	@Column(name = "EmployeeId", length = 100)
	private String employeeId;

	@Column(name = "UserType", length = 100)
	private String userType;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TokenGenerationTime", length = 100)
	private Date tokenGenerationTime;

	@Column(name = "AuthorizationToken", length = 255)
	private String authorizationToken;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MobTokenGenerationTime", length = 100)
	private Date mobTokenGenerationTime;

	@Column(name = "MobAuthorizationToken", length = 255)
	private String mobAuthorizationToken;

	
	@Column(name = "Gender", length = 100)
	private String gender;

	@Column(name = "DeviceStatus", length = 30)
	private String deviceStatus;
	
	@Column(name = "Country", length = 200)
	private String country;

	@Column(name = "StateName", length = 200)
	private String stateName;

	@Column(name = "CityName", length = 200)
	private String cityName;

	@Column(name = "Pincode", length = 10)
	private int pinCode;

	@Column(name = "EmailId", length = 200)
	private String emailId;

	@Column(name = "FirstName", length = 200)
	private String firstName;
	
	@Column(name = "AliasName", length = 200)
	private String aliasName;

	@Column(name = "MiddleName", length = 200)
	private String middleName;

	@Column(name = "LastName", length = 200)
	private String lastName;

	@Column(name = "LatitudeLongitude", length = 250)
	private String latitudeLongitude;

	@Column(name = "EmployeeProfilePic")
	private String employeeProfilePic;

	@Column(name = "DeviceLatitudeLongitude", length = 200)
	private String deviceLatitudeLongitude;

	@Column(name = "MobileNumber", length = 200)
	private String mobileNumber;

	@Column(name = "PanicNumber", length = 200)
	private String panicNumber;
	
	@Column(name = "SecondaryPanicNumber", length = 200)
	private String secondaryPanicNumber;
	
	
	@Column(name = "PhysicallyChallenged", length = 50)
	private String physicallyChallenged;
	
	@Column(name = "PragnentLady", length = 50)
    private String pragnentLady;
	
	@Column(name = "IsVip", length = 50)
    private String isVIP;
	
	@Column(name = "IsInjured", length = 50)
    private String isInjured;

	@Column(name = "password", length = 250)
	private String password;

	@Column(name = "HostMobileNumber", length = 100)
	private String hostMobileNumber;

	@Column(name = "ReportingManagerUserId", length =20)
	private int reportingManagerUserId;


	@Column(name = "GuestMiddleLoc", length = 100)
	private String guestMiddleLoc;

	@Column(name = "Status", length = 10)
	private String status;

	@Column(name = "WeekOffDays", length = 50)
	private String weekOffDays;

	@Column(name = "UpdatedBy", length = 50)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UpdatedTime", length = 30)
	private Date updatedTime;

	@Column(name = "userName", length = 200)
	private String userName;

	@Column(name = "EmployeeBusinessUnit", length = 200)
	private String employeeBusinessUnit;

	@Column(name = "EmployeeDepartment", length = 200)
	private String employeeDepartment;

	@Column(name = "EmployeeDomain", length = 200)
	private String employeeDomain;

	@Column(name = "Distance", length = 100)
	private double distance;
	
	@Column(name = "GeoCodeVariationDistance", length = 100)
	private double geoCodeVariationDistance;
	

	@Column(name = "TempCode", length = 50)
	private String tempCode;
	
	@Column(name = "WrongPassAttempt")
	private int wrongPassAttempt;

	@Transient
	private String buttonType;

	@Transient
	private String branchCode;

	@Transient
	private String branchName;
	
	@Transient
	private String projectName;
	
	@Transient
	private String facilityName;
	
	@Transient
	private String spocEmployeeId;
	
	@Transient
	private int branchId;
	
	@Transient
	private int routeId;
	
	@Transient
	private int selectedEmployeeUserId;
	
	
	@Transient
	private int nodalPointId;
	
	@Transient
	private int areaId;
	
	
	@Transient
	private String projectIds;
	

	@Transient
	private String combinedFacility;
	
	
	@Transient
	private int startPgNo;
	
	@Transient
	private int endPgNo;
	

	
	// bi-directional many-to-one association to eFmFmRouteAreaMapping
	@ManyToOne
	@JoinColumn(name = "RouteAreaId")
	private EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping;

	// bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name = "BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;

	// bi-directional many-to-one association to eFmFmClientProjectDetails
	@ManyToOne
	@JoinColumn(name = "ProjectId")
	private EFmFmClientProjectDetailsPO eFmFmClientProjectDetails;

	// bi-directional many-to-one association to EFmFmAlertTypeMaster
	@OneToMany(mappedBy = "efmFmUserMaster")
	private List<EFmFmAlertTypeMasterPO> efmFmAlertTypeMasters;

	// bi-directional many-to-one association to eFmFmAlertTxn
	@OneToMany(mappedBy = "efmFmUserMaster")
	private List<EFmFmAlertTxnPO> eFmFmAlertTxn;

	// bi-directional many-to-one association to EFmFmClientUserRole
	@OneToMany(mappedBy = "efmFmUserMaster")
	private List<EFmFmClientUserRolePO> efmFmClientUserRoles;

	// bi-directional many-to-one association to EFmFmDriverFeedback
	@OneToMany(mappedBy = "efmFmUserMaster")
	private List<EFmFmDriverFeedbackPO> efmFmDriverFeedbacks;
	
	@OneToMany(mappedBy = "efmFmUserMaster")
	private List<EFmFmLocationMasterPO> eFmFmLocationMaster;

	// bi-directional many-to-one association to eFmFmEmployeeRequestMaster
	@OneToMany(mappedBy = "efmFmUserMaster")
	private List<EFmFmEmployeeRequestMasterPO> eFmFmEmployeeRequestMaster;

	// bi-directional many-to-one association to EFmFmEmployeeTravelRequestPO
	@OneToMany(mappedBy = "efmFmUserMaster")
	private List<EFmFmEmployeeTravelRequestPO> eFmFmEmployeeTravelRequestPO;
	
	
	// bi-directional many-to-one association to eFmFmUserPassword
    @OneToMany(mappedBy = "efmFmUserMaster")
    private List<EFmFmUserPasswordPO> eFmFmUserPassword;


	// bi-directional many-to-one association to EFmFmVehicleInspectionPO
	@OneToMany(mappedBy = "efmFmUserMaster")
	private List<EFmFmVehicleInspectionPO> eFmFmVehicleInspection;
	
	// bi-directional many-to-one association to EFmFmEmployeeProjectDetailsPO
	@OneToMany(mappedBy = "efmFmUserMaster")
	private List<EFmFmEmployeeProjectDetailsPO> eFmFmEmployeeProjectDetailsPO;
	
	
	// bi-directional many-to-one association to eFmFmIndicationMasterPO
	@OneToMany(mappedBy = "efmFmUserMaster")
	private List<EFmFmIndicationMasterPO> eFmFmIndicationMasterPO;

	// bi-directional many-to-one association to eFmFmAdminCustomMessagePO
	@OneToMany(mappedBy = "efmFmUserMaster")
	private List<EFmFmAdminCustomMessagePO> eFmFmAdminCustomMessagePO;
	// bi-directional many-to-one association to EFmFmAdminSentSMSPO
	@OneToMany(mappedBy = "efmFmUserMaster")
	private List<EFmFmAdminSentSMSPO> eFmFmAdminSentSMSPO;

	public EFmFmUserMasterPO() {
	}

	public String getEmployeeBusinessUnit() {
		return employeeBusinessUnit;
	}

	public void setEmployeeBusinessUnit(String employeeBusinessUnit) {
		this.employeeBusinessUnit = employeeBusinessUnit;
	}

	public String getEmployeeDepartment() {
		return employeeDepartment;
	}

	public void setEmployeeDepartment(String employeeDepartment) {
		this.employeeDepartment = employeeDepartment;
	}

	public String getEmployeeDomain() {
		return employeeDomain;
	}

	public void setEmployeeDomain(String employeeDomain) {
		this.employeeDomain = employeeDomain;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getPinCode() {
		return pinCode;
	}

	public void setPinCode(int pinCode) {
		this.pinCode = pinCode;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getDateOfBirth() {
		return DateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		DateOfBirth = dateOfBirth;
	}

	public List<EFmFmEmployeeRequestMasterPO> geteFmFmEmployeeRequestMaster() {
		return eFmFmEmployeeRequestMaster;
	}

	public void seteFmFmEmployeeRequestMaster(List<EFmFmEmployeeRequestMasterPO> eFmFmEmployeeRequestMaster) {
		this.eFmFmEmployeeRequestMaster = eFmFmEmployeeRequestMaster;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public String getWeekOffDays() {
		return weekOffDays;
	}

	public void setWeekOffDays(String weekOffDays) {
		this.weekOffDays = weekOffDays;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLatitudeLongitude() {
		return latitudeLongitude;
	}

	public void setLatitudeLongitude(String latitudeLongitude) {
		this.latitudeLongitude = latitudeLongitude;
	}

	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<EFmFmAlertTypeMasterPO> getEfmFmAlertTypeMasters() {
		return this.efmFmAlertTypeMasters;
	}

	public void setEfmFmAlertTypeMasters(List<EFmFmAlertTypeMasterPO> efmFmAlertTypeMasters) {
		this.efmFmAlertTypeMasters = efmFmAlertTypeMasters;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceLatitudeLongitude() {
		return deviceLatitudeLongitude;
	}

	public void setDeviceLatitudeLongitude(String deviceLatitudeLongitude) {
		this.deviceLatitudeLongitude = deviceLatitudeLongitude;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getEmployeeDesignation() {
		return employeeDesignation;
	}

	public void setEmployeeDesignation(String employeeDesignation) {
		this.employeeDesignation = employeeDesignation;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public String getPanicNumber() {
		return panicNumber;
	}

	public void setPanicNumber(String panicNumber) {
		this.panicNumber = panicNumber;
	}

	public String getPhysicallyChallenged() {
		return physicallyChallenged;
	}

	public void setPhysicallyChallenged(String physicallyChallenged) {
		this.physicallyChallenged = physicallyChallenged;
	}

	public EFmFmRouteAreaMappingPO geteFmFmRouteAreaMapping() {
		return eFmFmRouteAreaMapping;
	}

	public void seteFmFmRouteAreaMapping(EFmFmRouteAreaMappingPO eFmFmRouteAreaMapping) {
		this.eFmFmRouteAreaMapping = eFmFmRouteAreaMapping;
	}

	public List<EFmFmDriverFeedbackPO> getEfmFmDriverFeedbacks() {
		return efmFmDriverFeedbacks;
	}

	public void setEfmFmDriverFeedbacks(List<EFmFmDriverFeedbackPO> efmFmDriverFeedbacks) {
		this.efmFmDriverFeedbacks = efmFmDriverFeedbacks;
	}

	public EFmFmAlertTypeMasterPO addEfmFmAlertTypeMaster(EFmFmAlertTypeMasterPO efmFmAlertTypeMaster) {
		getEfmFmAlertTypeMasters().add(efmFmAlertTypeMaster);
		efmFmAlertTypeMaster.setEfmFmUserMaster(this);

		return efmFmAlertTypeMaster;
	}

	public EFmFmAlertTypeMasterPO removeEfmFmAlertTypeMaster(EFmFmAlertTypeMasterPO efmFmAlertTypeMaster) {
		getEfmFmAlertTypeMasters().remove(efmFmAlertTypeMaster);
		efmFmAlertTypeMaster.setEfmFmUserMaster(null);

		return efmFmAlertTypeMaster;
	}

	public List<EFmFmClientUserRolePO> getEfmFmClientUserRoles() {
		return this.efmFmClientUserRoles;
	}

	public void setEfmFmClientUserRoles(List<EFmFmClientUserRolePO> efmFmClientUserRoles) {
		this.efmFmClientUserRoles = efmFmClientUserRoles;
	}

	public List<EFmFmAlertTxnPO> geteFmFmAlertTxn() {
		return eFmFmAlertTxn;
	}

	public void seteFmFmAlertTxn(List<EFmFmAlertTxnPO> eFmFmAlertTxn) {
		this.eFmFmAlertTxn = eFmFmAlertTxn;
	}

	public EFmFmClientUserRolePO addEfmFmClientUserRole(EFmFmClientUserRolePO efmFmClientUserRole) {
		getEfmFmClientUserRoles().add(efmFmClientUserRole);
		efmFmClientUserRole.setEfmFmUserMaster(this);

		return efmFmClientUserRole;
	}

	public EFmFmClientUserRolePO removeEfmFmClientUserRole(EFmFmClientUserRolePO efmFmClientUserRole) {
		getEfmFmClientUserRoles().remove(efmFmClientUserRole);
		efmFmClientUserRole.setEfmFmUserMaster(null);

		return efmFmClientUserRole;
	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	public List<EFmFmEmployeeTravelRequestPO> geteFmFmEmployeeTravelRequestPO() {
		return eFmFmEmployeeTravelRequestPO;
	}

	public void seteFmFmEmployeeTravelRequestPO(List<EFmFmEmployeeTravelRequestPO> eFmFmEmployeeTravelRequestPO) {
		this.eFmFmEmployeeTravelRequestPO = eFmFmEmployeeTravelRequestPO;
	}

	public EFmFmClientProjectDetailsPO geteFmFmClientProjectDetails() {
		return eFmFmClientProjectDetails;
	}

	public void seteFmFmClientProjectDetails(EFmFmClientProjectDetailsPO eFmFmClientProjectDetails) {
		this.eFmFmClientProjectDetails = eFmFmClientProjectDetails;
	}

	public String getLocationStatus() {
		return locationStatus;
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

	public void setLocationStatus(String locationStatus) {
		this.locationStatus = locationStatus;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<EFmFmVehicleInspectionPO> geteFmFmVehicleInspection() {
		return eFmFmVehicleInspection;
	}

	public void seteFmFmVehicleInspection(List<EFmFmVehicleInspectionPO> eFmFmVehicleInspection) {
		this.eFmFmVehicleInspection = eFmFmVehicleInspection;
	}

	public String getButtonType() {
		return buttonType;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}

	public String getTempCode() {
		return tempCode;
	}

	public void setTempCode(String tempCode) {
		this.tempCode = tempCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getEmployeeProfilePic() {
		return employeeProfilePic;
	}

	public Date getPasswordChangeDate() {
		return passwordChangeDate;
	}

	public void setPasswordChangeDate(Date passwordChangeDate) {
		this.passwordChangeDate = passwordChangeDate;
	}

	public void setEmployeeProfilePic(String employeeProfilePic) {
		this.employeeProfilePic = employeeProfilePic;
	}

	public int getWrongPassAttempt() {
		return wrongPassAttempt;
	}

	public void setWrongPassAttempt(int wrongPassAttempt) {
		this.wrongPassAttempt = wrongPassAttempt;
	}

    public Date getWrongPassAttemptDate() {
        return wrongPassAttemptDate;
    }

    public void setWrongPassAttemptDate(Date wrongPassAttemptDate) {
        this.wrongPassAttemptDate = wrongPassAttemptDate;
    }

    public String getPragnentLady() {
        return pragnentLady;
    }

    public void setPragnentLady(String pragnentLady) {
        this.pragnentLady = pragnentLady;
    }

    public String getIsInjured() {
        return isInjured;
    }

    public void setIsInjured(String isInjured) {
        this.isInjured = isInjured;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public boolean isTempPassWordChange() {
        return tempPassWordChange;
    }

    public void setTempPassWordChange(boolean tempPassWordChange) {
        this.tempPassWordChange = tempPassWordChange;
    }

    public List<EFmFmUserPasswordPO> geteFmFmUserPassword() {
        return eFmFmUserPassword;
    }

    public void seteFmFmUserPassword(List<EFmFmUserPasswordPO> eFmFmUserPassword) {
        this.eFmFmUserPassword = eFmFmUserPassword;
    }

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	public int getNodalPointId() {
		return nodalPointId;
	}

	public void setNodalPointId(int nodalPointId) {
		this.nodalPointId = nodalPointId;
	}

	public String getSecondaryPanicNumber() {
		return secondaryPanicNumber;
	}

	public void setSecondaryPanicNumber(String secondaryPanicNumber) {
		this.secondaryPanicNumber = secondaryPanicNumber;
	}

	public Date getoTPGenratingDate() {
		return oTPGenratingDate;
	}

	public void setoTPGenratingDate(Date oTPGenratingDate) {
		this.oTPGenratingDate = oTPGenratingDate;
	}

	public int getoTPGenratingCount() {
		return oTPGenratingCount;
	}

	public void setoTPGenratingCount(int oTPGenratingCount) {
		this.oTPGenratingCount = oTPGenratingCount;
	}

	public String getAuthorizationToken() {
		return authorizationToken;
	}

	public void setAuthorizationToken(String authorizationToken) {
		this.authorizationToken = authorizationToken;
	}

	public Date getTokenGenerationTime() {
		return tokenGenerationTime;
	}

	public void setTokenGenerationTime(Date tokenGenerationTime) {
		this.tokenGenerationTime = tokenGenerationTime;
	}

	public List<EFmFmLocationMasterPO> geteFmFmLocationMaster() {
		return eFmFmLocationMaster;
	}

	public void seteFmFmLocationMaster(List<EFmFmLocationMasterPO> eFmFmLocationMaster) {
		this.eFmFmLocationMaster = eFmFmLocationMaster;
	}

	public int getReportingManagerUserId() {
		return reportingManagerUserId;
	}

	public void setReportingManagerUserId(int reportingManagerUserId) {
		this.reportingManagerUserId = reportingManagerUserId;
	}

	public List<EFmFmEmployeeProjectDetailsPO> geteFmFmEmployeeProjectDetailsPO() {
		return eFmFmEmployeeProjectDetailsPO;
	}

	public void seteFmFmEmployeeProjectDetailsPO(List<EFmFmEmployeeProjectDetailsPO> eFmFmEmployeeProjectDetailsPO) {
		this.eFmFmEmployeeProjectDetailsPO = eFmFmEmployeeProjectDetailsPO;
	}

	public List<EFmFmIndicationMasterPO> geteFmFmIndicationMasterPO() {
		return eFmFmIndicationMasterPO;
	}

	public void seteFmFmIndicationMasterPO(List<EFmFmIndicationMasterPO> eFmFmIndicationMasterPO) {
		this.eFmFmIndicationMasterPO = eFmFmIndicationMasterPO;
	}

	public Date getMobTokenGenerationTime() {
		return mobTokenGenerationTime;
	}

	public void setMobTokenGenerationTime(Date mobTokenGenerationTime) {
		this.mobTokenGenerationTime = mobTokenGenerationTime;
	}

	public String getMobAuthorizationToken() {
		return mobAuthorizationToken;
	}

	public void setMobAuthorizationToken(String mobAuthorizationToken) {
		this.mobAuthorizationToken = mobAuthorizationToken;
	}

	public String getIsVIP() {
		return isVIP;
	}

	public void setIsVIP(String isVIP) {
		this.isVIP = isVIP;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public List<EFmFmAdminCustomMessagePO> geteFmFmAdminCustomMessagePO() {
		return eFmFmAdminCustomMessagePO;
	}

	public void seteFmFmAdminCustomMessagePO(List<EFmFmAdminCustomMessagePO> eFmFmAdminCustomMessagePO) {
		this.eFmFmAdminCustomMessagePO = eFmFmAdminCustomMessagePO;
	}

	public List<EFmFmAdminSentSMSPO> geteFmFmAdminSentSMSPO() {
		return eFmFmAdminSentSMSPO;
	}

	public void seteFmFmAdminSentSMSPO(List<EFmFmAdminSentSMSPO> eFmFmAdminSentSMSPO) {
		this.eFmFmAdminSentSMSPO = eFmFmAdminSentSMSPO;
	}

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}

	public String getOHRId() {
		return OHRId;
	}

	public void setOHRId(String oHRId) {
		OHRId = oHRId;
	}
//
//	public String getProjectIds() {
//		return projectIds;
//	}
//
//	public void setProjectIds(String projectIds) {
//		this.projectIds = projectIds;
//	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public String getSpocEmployeeId() {
		return spocEmployeeId;
	}

	public void setSpocEmployeeId(String spocEmployeeId) {
		this.spocEmployeeId = spocEmployeeId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getSelectedEmployeeUserId() {
		return selectedEmployeeUserId;
	}

	public void setSelectedEmployeeUserId(int selectedEmployeeUserId) {
		this.selectedEmployeeUserId = selectedEmployeeUserId;
	}

	public int getStartPgNo() {
		return startPgNo;
	}

	public void setStartPgNo(int startPgNo) {
		this.startPgNo = startPgNo;
	}

	public int getEndPgNo() {
		return endPgNo;
	}

	public void setEndPgNo(int endPgNo) {
		this.endPgNo = endPgNo;
	}

	public String getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(String projectIds) {
		this.projectIds = projectIds;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getGeoCodedAddress() {
		return geoCodedAddress;
	}

	public void setGeoCodedAddress(String geoCodedAddress) {
		this.geoCodedAddress = geoCodedAddress;
	}

	public String getHomeGeoCodePoints() {
		return homeGeoCodePoints;
	}

	public void setHomeGeoCodePoints(String homeGeoCodePoints) {
		this.homeGeoCodePoints = homeGeoCodePoints;
	}

	public double getGeoCodeVariationDistance() {
		return geoCodeVariationDistance;
	}

	public void setGeoCodeVariationDistance(double geoCodeVariationDistance) {
		this.geoCodeVariationDistance = geoCodeVariationDistance;
	}
    
}