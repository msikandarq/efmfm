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

/**
 * The persistent class for the eFmFmClientBranchPO database table.
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "eFmFmClientBranchPO")
public class EFmFmClientBranchPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BranchId", length = 50)
	private int branchId;

	@Column(name = "StateName", length = 100)
	private String stateName;

	@Column(name = "CityName", length = 100)
	private String cityName;

	@Column(name = "Pincode", length = 50)
	private int pinCode;

	@Column(name = "Address", length = 250)
	private String address;

	@Column(name = "AuthorizationToken", length = 255)
	private String authorizationToken;
	
	
	@Column(name = "GeoCodesForGeoFence", length = 255)
	private String geoCodesForGeoFence;

	@Column(name = "BranchDescription", length = 255)
	private String branchDescription;

	@Column(name = "BranchUri", length = 255)
	private String branchUri;

	
	
	@Column(name = "EmployeeChecKInVia", length = 100)
	private String employeeChecKInVia;

	@Column(name = "CountryCode", length = 10)
	private String countryCode;
	
	@Column(name = "OnPickUpNoShowCancelDrop", length = 10)
	private String onPickUpNoShowCancelDrop;
	
	

	@Column(name = "CreatedBy", length = 50)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreationTime", length = 100)
	private Date creationTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TakenGenrationTime", length = 100)
	private Date takenGenrationTime;


	@Column(name = "EmailId", length = 100)
	private String emailId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EndDate", length = 100)
	private Date endDate;

	@Column(name = "LatitudeLongitude", length = 100)
	private String latitudeLongitude;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "StartDate", length = 100)
	private Date startDate;

	@Column(name = "Status", length = 100)
	private String status;

	@Column(name = "ManagerApprovalRequired", length = 50)
	private String mangerApprovalRequired;

	@Column(name = "EscortRequired", length = 50)
	private String escortRequired;

	@Column(name = "UpdatedBy", length = 50)
	private String updatedBy;
	
	@Column(name = "DistanceFlg", length = 50)
	private String distanceFlg;
	
	
	@Column(name = "DriverWaitingTimeAtLastLocation", length = 100)
	private Time driverWaitingTimeAtLastLocation;
	
	
	@Column(name = "SelectedB2bType", length = 50)
	private String selectedB2bType;
	
	
	@Column(name = "B2bByTravelDistanceInKM", length = 50)
	private double b2bByTravelDistanceInKM;
	
	@Column(name = "B2bByTravelTime", length = 100)
	private Time b2bByTravelTime;
	

	@Column(name = "BranchCode", length = 150)
	private String branchCode;
	
	@Column(name = "BranchName", length = 150)
	private String branchName;
	
	@Column(name = "RequestType", length = 50)
	private String requestType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UpdatedTime", length = 30)
	private Date updatedTime;
	
	
	@Column(name = "DissableTimeingOTP")
	 private Time dissableTimeOTP;
	 
	
	@Column(name = "TripConsiderDelayAfter")
	 private Time tripConsiderDelayAfter;
	
	
	 @Column(name = "MaxTimeOTP")
	 private int maxTimeOTP;
	 
	 @Column(name = "NumberOfConsecutiveNoShow")
	 private int numberOfConsecutiveNoShow;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RequestCutOffDate", length = 30)
	private Date requestCutOffDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RequestCutOffFromDate", length = 30)
	private Date requestCutOffFromDate;
	
	@Column(name="ImageUploadSize", length=10)
	private int imageUploadSize;
	
	@Column(name = "RequestCutOffNoOfDays", length = 50)
	private int requestCutOffNoOfDays;

	@Column(name = "etaSMS", length = 50)
	private int etaSMS;
	
	@Column(name="DriverAutoCheckoutStatus", length=100)
	private String driverAutoCheckoutStatus;
	
	@Column(name="AutoVehicleAllocationStatus", length=100)
	private String autoVehicleAllocationStatus;

	@Column(name = "EmployeeAddressgeoFenceArea", length = 50)
	private int employeeAddressgeoFenceArea;

	// Defined Geofence Regions for auto routing
	@Column(name = "EmployeeAreaGeofenceRegion", length = 50)
	private int employeeAreaGeofenceRegion;

	@Column(name = "AreaToClusterGeofenceRegion", length = 50)
	private int areaToClusterGeofenceRegion;

	@Column(name = "VehicleAllocationGeofenceRegion", length = 50)
	private int vehicleAllocationGeofenceRegion;

	@Column(name = "ShiftTimePlusOneHourrAfterSMSContact", length = 50)
	private String shiftTimePlusOneHourrAfterSMSContact;

	@Column(name = "ShiftTimePlusTwoHourrAfterSMSContact", length = 50)
	private String shiftTimePlusTwoHourrAfterSMSContact;

	@Column(name = "MaxSpeed", length = 50)
	private int maxSpeed;

	@Column(name = "DriverAutoCheckedoutTime", length = 50)
	private Time driverAutoCheckedoutTime;

	@Column(name = "DelayMessageTime", length = 50)
	private int delayMessageTime;

	@Column(name = "EmployeeWaitingTime", length = 50)
	private int employeeWaitingTime;

	@Column(name = "EmergencyContactNumber", length = 100)
	private String emergencyContactNumber;
	
	@Column(name = "SecondEmergencyContactNumber", length = 100)
	private String secondEmergencyContactNumber;

	@Column(name = "MaxTravelTimeEmployeeWiseInMin", length = 50)
	private int maxTravelTimeEmployeeWiseInMin;

	@Column(name = "MaxRadialDistanceEmployeeWiseInKm", length = 50)
	private int maxRadialDistanceEmployeeWiseInKm;

	@Column(name = "MaxRouteLengthInKm", length = 50)
	private int maxRouteLengthInKm;

	@Column(name = "MaxRouteDeviationInKm", length = 50)
	private int maxRouteDeviationInKm;

	@Column(name = "TransportContactNumberForMsg", length = 50)
	private String transportContactNumberForMsg;

	@Column(name = "EnableAutoClustering", length = 50)
	private String autoClustering;

	@Column(name = "ClusterSize")
	private int clusterSize;

	@Column(name = "StartTripGeoFenceAreaInMeter", length = 50)
	private int startTripGeoFenceAreaInMeter;

	@Column(name = "EndTripGeoFenceAreaInMeter", length = 50)
	private int endTripGeoFenceAreaInMeter;

	@Column(name = "FeedBackEmailId", length = 100)
	private String feedBackEmailId;

	@Column(name = "TripType", length = 50)
	private String tripType;

	@Transient
	private String mobileNumber;

	@Column(name = "NumberOfAdministarator")
	private int numberOfAdministarator;

	@Column(name = "PasswordResetPeriodForAdminInDays")
	private int passwordResetPeriodForAdminInDays;

	@Column(name = "PasswordResetPeriodForUserInDays")
	private int passwordResetPeriodForUserInDays;

	@Column(name = "TwoFactorAuthenticationRequired")
	private String twoFactorAuthenticationRequired;

	@Column(name = "SessionTimeoutInMinutes")
	private int sessionTimeoutInMinutes;
	
	@Column(name = "SessionNotificationTime")
	private int sessionNotificationTime;
	

	@Column(name = "NumberOfAttemptsWrongPass")
	private int numberOfAttemptsWrongPass;

	@Column(name = "EmpDeviceDriverImage", length = 255)
	private String empDeviceDriverImage;

	@Column(name = "EmpDeviceDriverMobileNumber", length = 255)
	private String empDeviceDriverMobileNumber;

	@Column(name = "EmpDeviceDriverName")
	private String empDeviceDriverName;

	@Column(name = "DriverDeviceAutoCallAndsms", length = 55)
	private String driverDeviceAutoCallAndsms;

	@Column(name = "DriverDeviceDriverProfilePicture")
	private String driverDeviceDriverProfilePicture;

	@Column(name = "InvoiceNumberDigitRange")
	private int invoiceNumberDigitRange;

	@Column(name = "AdhocTimePickerForEmployee", length = 50)
	private String adhocTimePickerForEmployee;

	@Column(name = "ReschedulePickupCutOffTime")
	private Time reschedulePickupCutOffTime;

	@Column(name = "RescheduleDropCutOffTime")
	private Time rescheduleDropCutOffTime;

	@Column(name = "DropPriorTimePeriod", length = 30)
	private Time dropPriorTimePeriod;

	@Column(name = "PickupPriorTimePeriod")
	private Time pickupPriorTimePeriod;

	@Column(name = "DropCancelTimePeriod")
	private Time dropCancelTimePeriod;

	@Column(name = "PickupCancelTimePeriod")
	private Time pickupCancelTimePeriod;

	@Column(name = "PanicAlertNeeded", length = 50)
	private String panicAlertNeeded;

	@Column(name = "ShiftTimeDiffPickToDrop")
	private Time shiftTimeDiffPickToDrop;

	@Column(name = "AddingGeoFenceDistanceIntrip", length = 50)
	private int addingGeoFenceDistanceIntrip;

	@Column(name = "AutoDropRoster", length = 50)
	private String autoDropRoster;
	
	private String escortTimeWindowEnable;

	// employee request time between two shifts.
	@Column(name = "EmployeeSecondPickUpRequest", length = 50)
	private int employeeSecondPickUpRequest;

	@Column(name = "EmployeeSecondDropRequest", length = 50)
	private int employeeSecondDropRequest;

	@Column(name = "LastPassCanNotCurrentPass", length = 50)
	private int lastPassCanNotCurrentPass;

	@Column(name = "InactiveAdminAccountAfterNumOfDays", length = 50)
	private int inactiveAdminAccountAfterNumOfDays;

	@Column(name = "CutOffTime")
	private String cutOffTime;

	@Column(name = "InvoiceGenDate", columnDefinition = "Decimal(10) default '0'", length = 50)
	private int invoiceGenDate;

	@Column(name = "InvoiceNoOfDWorkingDays", columnDefinition = "Decimal(10) default '0'", length = 50)
	private int invoiceNoOfDWorkingDays;
	
	@Column(name = "EarlyRequestDate", length = 50)
	private int earlyRequestDate;
	
	@Column(name = "OccurrenceFlg", length = 10)
	private String occurrenceFlg;
	
	@Column(name = "MonthOrDays", length = 200)
	private String monthOrDays;
	
	@Column(name = "DaysRequest", length = 200)
	private String daysRequest;
	/*
	@Column(name = "RequestLocation", length = 200)
	private String requestLocation;*/
	
	@Column(name = "LocationVisible", length = 200)
	private String locationVisible;
	

	@Column(name = "InvoiceGenType",length = 200)
	private String invoiceGenType;
	
	@Column(name = "DestinationPointLimit",length =10)
	private int destinationPointLimit;
	
	
	
	@Column(name = "NotificationCutoffTimeForPickup")
	private Time notificationCutoffTimeForPickup;
	
	@Column(name = "NotificationCutoffTimeForDrop")
	private Time notificationCutoffTimeForDrop;
	
	@Column(name = "PersonalDeviceViaSms",length =10)
	private String personalDeviceViaSms;
	
	@Column(name = "AssignRoutesToVendor",length =10)
	private String assignRoutesToVendor;
	
	@Column(name = "DriverCallToEmployee",length =10)
	private String driverCallToEmployee;
	
	@Column(name = "DriverCallToTransportDesk",length =10)
	private String driverCallToTransportDesk;

	@Column(name = "EmployeeCallToDriver",length =10)
	private String employeeCallToDriver;
	
	@Column(name = "EmployeeCallToTransport",length =10)
	private String employeeCallToTransport;
	
	@Column(name = "EscortTimeStatus",length =50)
	private String escortTimeStatus;	
	
	
	@Column(name = "EmployeeFeedbackEmail",length =50)
	private String employeeFeedbackEmail;
	
	@Column(name = "ToEmployeeFeedBackEmail",length =50)
	private String toEmployeeFeedBackEmail;
	
	@Column(name = "EmployeeFeedbackEmailId", length = 200)
	private String employeeFeedbackEmailId;
	
	@Column(name = "EmployeeAppReportBug", length = 200)
	private String employeeAppReportBug;
	
	
	@Column(name = "ReportBugEmailIds", length = 200)
	private String reportBugEmailIds;
	
	
	@Column(name = "EmployeeCheckOutGeofenceRegion", length = 50)
	private int employeeCheckOutGeofenceRegion;
			
	
	@Column(name = "EscortStartTimePickup")
	private Time escortStartTimePickup;
	
	
	@Column(name = "EscortEndTimePickup")
	private Time escortEndTimePickup;
	
	@Column(name = "EscortStartTimeDrop")
	private Time escortStartTimeDrop;
	
	@Column(name = "EscortEndTimeDrop")
	private Time escortEndTimeDrop;
	
	@Transient
	private String escortStartTimeForPickup;
	
	
	@Transient
	private String escortEndTimeForPickup;
	
	@Transient
	private String escortStartTimeForDrop;
	
	@Transient
	private String escortEndTimeForDrop;
	

	@Column(name = "LicenseExpiryDay", length = 50)
	private int licenseExpiryDay;

	@Column(name = "LicenseRepeatAlertsEvery", length = 50)
	private int licenseRepeatAlertsEvery;
	
	@Column(name = "LicenceNotificationType", length = 50)
	private String licenceNotificationType;
	
	@Column(name = "LicenseSMSNumber", length = 50)
	private String licenseSMSNumber;
	
	@Column(name = "LicenseEmailId", length = 50)
	private String licenseEmailId;

	@Column(name = "MedicalFitnessExpiryDay", length = 50)
	private int medicalFitnessExpiryDay;
	
	@Column(name = "MedicalFitnessRepeatAlertsEvery", length = 50)
	private int medicalFitnessRepeatAlertsEvery;
	
	@Column(name = "MedicalFitnessNotificationType", length = 50)
	private String medicalFitnessNotificationType;
	
	@Column(name = "MedicalFitnessSMSNumber", length = 50)
	private String medicalFitnessSMSNumber;
	
	@Column(name = "MedicalFitnessEmailId", length = 50)
	private String medicalFitnessEmailId;

	@Column(name = "PoliceVerificationExpiryDay", length = 50)
	private int policeVerificationExpiryDay;
	
	@Column(name = "PoliceVerificationRepeatAlertsEvery", length = 50)
	private int policeVerificationRepeatAlertsEvery;
	
	@Column(name = "PoliceVerificationNotificationType", length = 50)
	private String policeVerificationNotificationType;
	
	@Column(name = "PoliceVerificationSMSNumber", length = 50)
	private String policeVerificationSMSNumber;
	
	@Column(name = "PoliceVerificationEmailId", length = 50)
	private String policeVerificationEmailId;

	@Column(name = "DDTrainingExpiryDay", length = 50)
	private int ddTrainingExpiryDay;
	
	@Column(name = "DDTrainingRepeatAlertsEvery", length = 50)
	private int ddTrainingRepeatAlertsEvery;
	
	@Column(name = "DDTrainingNotificationType", length = 50)
	private String ddTrainingNotificationType;
	
	@Column(name = "DDTrainingSMSNumber", length = 50)
	private String ddTrainingSMSNumber;
	
	
	@Column(name = "DDTrainingEmailId", length = 50)
	private String ddTrainingEmailId;

	@Column(name = "PollutionDueExpiryDay", length = 50)
	private int pollutionDueExpiryDay;
	
	@Column(name = "PollutionDueRepeatAlertsEvery", length = 50)
	private int pollutionDueRepeatAlertsEvery;
	
	@Column(name = "PollutionDueNotificationType", length = 50)
	private String pollutionDueNotificationType;
	
	@Column(name = "PollutionDueSMSNumber", length = 50)
	private String pollutionDueSMSNumber;
	
	@Column(name = "PollutionDueEmailId", length = 50)
	private String pollutionDueEmailId;

	
	@Column(name = "IsuranceDueExpiryDay", length = 50)
	private int insuranceDueExpiryDay;
	
	@Column(name = "InsuranceDueRepeatAlertsEvery", length = 50)
	private int insuranceDueRepeatAlertsEvery;
	
	@Column(name = "InsuranceDueNotificationType", length = 50)
	private String insuranceDueNotificationType;
	
	@Column(name = "InsuranceDueSMSNumber", length = 50)
	private String insuranceDueSMSNumber;
	
	@Column(name = "InsuranceDueEmailId", length = 50)
	private String insuranceDueEmailId;

	@Column(name = "TaxCertificateExpiryDay", length = 50)
	private int taxCertificateExpiryDay;
	
	@Column(name = "TaxCertificateRepeatAlertsEvery", length = 50)
	private int taxCertificateRepeatAlertsEvery;
	
	@Column(name = "TaxCertificateNotificationType", length = 50)
	private String taxCertificateNotificationType;
	
	@Column(name = "TaxCertificateSMSNumber", length = 50)
	private String taxCertificateSMSNumber;
	
	@Column(name = "TaxCertificateEmailId", length = 50)
	private String taxCertificateEmailId;

	@Column(name = "PermitDueExpiryDay", length = 50)
	private int permitDueExpiryDay;
	
	@Column(name = "PermitDueRepeatAlertsEvery", length = 50)
	private int permitDueRepeatAlertsEvery;
	
	@Column(name = "PermitDueNotificationType", length = 50)
	private String permitDueNotificationType;
	
	@Column(name = "PermitDueSMSNumber", length = 50)
	private String permitDueSMSNumber;
	
	@Column(name = "PermitDueEmailId", length = 50)
	private String permitDueEmailId;

	@Column(name = "VehicelMaintenanceExpiryDay", length = 50)
	private int vehicelMaintenanceExpiryDay;
	
	@Column(name = "VehicelMaintenanceRepeatAlertsEvery", length = 50)
	private int vehicelMaintenanceRepeatAlertsEvery;
	
	@Column(name = "VehicelMaintenanceNotificationType", length = 50)
	private String vehicelMaintenanceNotificationType;
	
	@Column(name = "VehicelMaintenanceSMSNumber", length = 50)
	private String vehicelMaintenanceSMSNumber;
	
	@Column(name = "VehicelMaintenanceEmailId", length = 50)
	private String vehicelMaintenanceEmailId;
	
	@Column(name = "ShiftTimeGenderPreference", length = 250)
	private String shiftTimeGenderPreference;	
	
	@Column(name = "ApprovalProcess", length = 50)
	private String approvalProcess;
	
	@Column(name = "PostApproval", length = 10)
	private int postApproval;
	
	@Column(name = "RequestWithProject", length = 10)
	private String requestWithProject;
	
	@Column(name = "VehiceCheckList", length = 10)
	private String vehiceCheckList;
	
	@Column(name = "DriverTripHistory", length = 10)
	private String driverTripHistory;
	
	/*@Column(name = "TripHistoryCount", length = 10)
	private int tripHistoryCount;*/
	
	@Column(name = "minimumDestinationCount", length = 10)
	private int minimumDestCount;
	
	@Column(name = "GPSKmModification", length = 10)
	private String gpsKmModification;
	
	@Column(name = "ManagerReqCreateProcess", length = 50)
	private String managerReqCreateProcess;
	
	@Column(name = "TranportCommunicationMailId", length = 200)
	private String tranportCommunicationMailId;
	
	
	@Column(name = "PlaCardPrint", length = 50)
	private String plaCardPrint;

	
	 @Transient
	 private String notificationCutoffTimePickup;
		
	 @Transient
	 private String notificationCutoffTimeDrop;
	 
	 @Column(name = "multiFacility",nullable = false)
	 private boolean multiFacility;
	 
	 @Column(name = "MobileLoginVia", length = 50)
	 private String mobileLoginVia;
	 
	 @Column(name = "MobileLoginUrl", length = 200)
	 private String mobileLoginUrl;
	 
	 @Column(name = "SsoLoginUrl", length = 200)
	 private String ssoLoginUrl;
	 
	 @Column(name = "MobilePageCount", columnDefinition = "Decimal(10) default '0'", length = 50)
	 private int mobilePageCount;
	 
	 
	 @Column(name = "WebPageCount", columnDefinition = "Decimal(10) default '0'", length = 50)
	 private int webPageCount;
	 
	 @Column(name = "GeoCodedAddress", length = 50)
	private String geoCodedAddress;
	 
	 
//	 @Transient
//	 private String escortStartWin;
	 
	 
	 @Transient
	 private String otpDisableTime;
	 
//	 @Transient
//	 private String escortEndWin;
	
	@Transient
	private String employeeId;
	
	@Transient
	private String shiftSelectType;
	
	@Transient
	private String time;
	
	@Transient
	private String modules;

	@Transient
	private String dropCutOffTime;

	@Transient
	private String pickupCutOffTime;

	@Transient
	private String dropCancelCutOffTime;
		
	@Transient
	private String driverLastLocWaitingTime;

	@Transient
	private String travelTimeFromDropToFirstPickUp;
	

	@Transient
	private String pickupCancelCutOffTime;

	@Transient
	private String dropPickupReqCutOfTime;

	@Transient
	private String reschedulePickupTime;
	@Transient
	private String rescheduleDropTime;
	
	@Transient
	private String shiftTime;

	@Transient
	private String driverAutoCheckOut;
	
	
	@Transient
	private int areaGeoFenceRegion;
	
	
	@Transient
	private int clusterGeoFenceRegion;
	
	
	@Transient
	private int routeGeoFenceRegion;
	
	@Transient
	private String requestToDateCutOff;
	
	@Transient
	private String requestFromDateCutOff;
	
	@Transient
	private String tripDelayTime;
	
	@Transient
	private String userRoles;
	
	@Transient
	private int userId;	
	
	
	@Transient
	private String combinedFacility;
	
	
	

	// bi-directional many-to-one association to EFmFmClientMaster
	@ManyToOne
	@JoinColumn(name = "ClientId")
	private EFmFmClientMasterPO eFmFmClientMaster;

	// bidirectional manytoone association to EFmFmAlertTypeMaster
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmAlertTypeMasterPO> efmFmAlertTypeMasters;

	// bidirectional manytoone association to EFmFmClientRouteMapping
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmClientRouteMappingPO> efmFmClientRouteMappings;

	// bidirectional manytoone association to EFmFmClientUserRole
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmClientUserRolePO> efmFmClientUserRoles;

	// bidirectional manytoone association to eFmFmShiftTiming
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmTripTimingMasterPO> eFmFmShiftTiming;

	// bidirectional manytoone association to EFmFmUserMaster
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmUserMasterPO> efmFmUserMasters;
	
	
	// bidirectional manytoone association to eFmFmVehicleCapacityMaster
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmVehicleCapacityMasterPO> eFmFmVehicleCapacityMaster;

	

	// bidirectional manytoone association to EFmFmVendorMaster
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmVendorMasterPO> efmFmVendorMasters;

	// bidirectional manytoone association to EFmFmVendorMaster
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmAssignRoutePO> eFmFmAssignRoute;

	// bidirectional manytoone association to eFmFmClientProjectDetails
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmClientProjectDetailsPO> eFmFmClientProjectDetails;

	// bidirectional manytoone association to EFmFmVendorMaster
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmActualRoutTravelledPO> eFmFmActualRouteTravelled;

	// bidirectional manytoone association to EFmFmVendorMaster
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmLiveRoutTravelledPO> eFmFmLiveRoutTravelledPO;

	// bidirectional manytoone association to eFmFmVendorContractMaster
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmVendorContractTypeMasterPO> eFmFmVendorContractTypeMaster;

	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmFuelChargesPO> eFmFmFuelCharge;

	// bidirectional manytoone association to eFmFmDeviceMaster
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmDeviceMasterPO> eFmFmDeviceMaster;

	// bidirectional manytoone association to eFmFmUserPassword
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmUserPasswordPO> eFmFmUserPassword;

	// bi-directional many-to-one association to EFmFmClientUserRole
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmClientBranchConfigurationMappingPO> eFmFmClientBranchConfigurationMapping;

	// bidirectional manytoone association to eFmFmRoutingArea
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmRoutingAreaPO> eFmFmRoutingArea;

	// bidirectional manytoone association to areaEmpClusterMapping
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<AreaEmpClusterMappingPO> areaEmpClusterMapping;

	// bidirectional manytoone association to eFmFmAlgoRoutes
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmAlgoRoutesPO> eFmFmAlgoRoutes;
	
		
	// bidirectional manytoone association to eFmFmAlgoRoutes
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmFieldAppConfigMasterPO> eFmFmFieldAppConfigMaster;
	
	// bidirectional manytoone association to eFmFmAlgoRoutes
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmDynamicVehicleCheckListPO> eFmFmDynamicVehicleCheckListPO;
	
	@OneToMany(mappedBy = "eFmFmClientBranchPO")
	private List<EFmFmSmsAlertMasterPO> eFmFmSmsAlertMaster;
	
	
	// bidirectional manytoone association to eFmFmAdminCustomMessagePO
	 @OneToMany(mappedBy = "eFmFmClientBranchPO")
	 private List<EFmFmAdminCustomMessagePO> eFmFmAdminCustomMessagePO;

	 // bidirectional manytoone association to EFmFmAdminSentSMSPO
	 @OneToMany(mappedBy = "eFmFmClientBranchPO")
	 private List<EFmFmAdminSentSMSPO> eFmFmAdminSentSMSPO;
	 
	// bidirectional manytoone association to eFmFmFacilityToFacilityMapping
		@OneToMany(mappedBy = "eFmFmClientBranchPO")
		private List<EFmFmFacilityToFacilityMappingPO> eFmFmFacilityToFacilityMapping;

		// bidirectional manytoone association to eFmFmUserFacilityMapping
		@OneToMany(mappedBy = "eFmFmClientBranchPO")
		private List<EFmFmUserFacilityMappingPO> eFmFmUserFacilityMapping;

	
		
	//bidirectional @OneToMany association to eFmFmEmployeeTravelRequest
	@OneToMany(mappedBy="eFmFmClientBranchPO")
	private List<EFmFmEmployeeTravelRequestPO> eFmFmEmployeeTravelRequest;
	
	
	
	//bidirectional @OneToMany association to eFmFmEmployeeRequestMaster
	@OneToMany(mappedBy="eFmFmClientBranchPO")
	private List<EFmFmEmployeeRequestMasterPO> eFmFmEmployeeRequestMaster;
	
	
	

	public EFmFmClientBranchPO() {
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public int getEmployeeAreaGeofenceRegion() {
		return employeeAreaGeofenceRegion;
	}

	public void setEmployeeAreaGeofenceRegion(int employeeAreaGeofenceRegion) {
		this.employeeAreaGeofenceRegion = employeeAreaGeofenceRegion;
	}

	public int getAreaToClusterGeofenceRegion() {
		return areaToClusterGeofenceRegion;
	}

	public void setAreaToClusterGeofenceRegion(int areaToClusterGeofenceRegion) {
		this.areaToClusterGeofenceRegion = areaToClusterGeofenceRegion;
	}

	public int getVehicleAllocationGeofenceRegion() {
		return vehicleAllocationGeofenceRegion;
	}

	public void setVehicleAllocationGeofenceRegion(int vehicleAllocationGeofenceRegion) {
		this.vehicleAllocationGeofenceRegion = vehicleAllocationGeofenceRegion;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLatitudeLongitude() {
		return latitudeLongitude;
	}

	public void setLatitudeLongitude(String latitudeLongitude) {
		this.latitudeLongitude = latitudeLongitude;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMangerApprovalRequired() {
		return mangerApprovalRequired;
	}

	public void setMangerApprovalRequired(String mangerApprovalRequired) {
		this.mangerApprovalRequired = mangerApprovalRequired;
	}

	public String getEscortRequired() {
		return escortRequired;
	}

	public void setEscortRequired(String escortRequired) {
		this.escortRequired = escortRequired;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getEtaSMS() {
		return etaSMS;
	}

	public void setEtaSMS(int etaSMS) {
		this.etaSMS = etaSMS;
	}

	public int getEmployeeAddressgeoFenceArea() {
		return employeeAddressgeoFenceArea;
	}

	public void setEmployeeAddressgeoFenceArea(int employeeAddressgeoFenceArea) {
		this.employeeAddressgeoFenceArea = employeeAddressgeoFenceArea;
	}

	public String getShiftTimePlusOneHourrAfterSMSContact() {
		return shiftTimePlusOneHourrAfterSMSContact;
	}

	public void setShiftTimePlusOneHourrAfterSMSContact(String shiftTimePlusOneHourrAfterSMSContact) {
		this.shiftTimePlusOneHourrAfterSMSContact = shiftTimePlusOneHourrAfterSMSContact;
	}

	public String getShiftTimePlusTwoHourrAfterSMSContact() {
		return shiftTimePlusTwoHourrAfterSMSContact;
	}

	public void setShiftTimePlusTwoHourrAfterSMSContact(String shiftTimePlusTwoHourrAfterSMSContact) {
		this.shiftTimePlusTwoHourrAfterSMSContact = shiftTimePlusTwoHourrAfterSMSContact;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public String getModules() {
		return modules;
	}

	public void setModules(String modules) {
		this.modules = modules;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public Time getDriverAutoCheckedoutTime() {
		return driverAutoCheckedoutTime;
	}

	public void setDriverAutoCheckedoutTime(Time driverAutoCheckedoutTime) {
		this.driverAutoCheckedoutTime = driverAutoCheckedoutTime;
	}

	public int getDelayMessageTime() {
		return delayMessageTime;
	}

	public void setDelayMessageTime(int delayMessageTime) {
		this.delayMessageTime = delayMessageTime;
	}

	public int getEmployeeWaitingTime() {
		return employeeWaitingTime;
	}

	public void setEmployeeWaitingTime(int employeeWaitingTime) {
		this.employeeWaitingTime = employeeWaitingTime;
	}

	public String getEmergencyContactNumber() {
		return emergencyContactNumber;
	}

	public String getGeoCodesForGeoFence() {
		return geoCodesForGeoFence;
	}

	public void setGeoCodesForGeoFence(String geoCodesForGeoFence) {
		this.geoCodesForGeoFence = geoCodesForGeoFence;
	}

	public void setEmergencyContactNumber(String emergencyContactNumber) {
		this.emergencyContactNumber = emergencyContactNumber;
	}

	public int getMaxTravelTimeEmployeeWiseInMin() {
		return maxTravelTimeEmployeeWiseInMin;
	}

	public void setMaxTravelTimeEmployeeWiseInMin(int maxTravelTimeEmployeeWiseInMin) {
		this.maxTravelTimeEmployeeWiseInMin = maxTravelTimeEmployeeWiseInMin;
	}

	public int getMaxRadialDistanceEmployeeWiseInKm() {
		return maxRadialDistanceEmployeeWiseInKm;
	}

	public void setMaxRadialDistanceEmployeeWiseInKm(int maxRadialDistanceEmployeeWiseInKm) {
		this.maxRadialDistanceEmployeeWiseInKm = maxRadialDistanceEmployeeWiseInKm;
	}

	public int getMaxRouteLengthInKm() {
		return maxRouteLengthInKm;
	}

	public void setMaxRouteLengthInKm(int maxRouteLengthInKm) {
		this.maxRouteLengthInKm = maxRouteLengthInKm;
	}

	public int getMaxRouteDeviationInKm() {
		return maxRouteDeviationInKm;
	}

	public void setMaxRouteDeviationInKm(int maxRouteDeviationInKm) {
		this.maxRouteDeviationInKm = maxRouteDeviationInKm;
	}

	public String getTransportContactNumberForMsg() {
		return transportContactNumberForMsg;
	}

	public void setTransportContactNumberForMsg(String transportContactNumberForMsg) {
		this.transportContactNumberForMsg = transportContactNumberForMsg;
	}

	public String getAutoClustering() {
		return autoClustering;
	}

	public void setAutoClustering(String autoClustering) {
		this.autoClustering = autoClustering;
	}

	public int getStartTripGeoFenceAreaInMeter() {
		return startTripGeoFenceAreaInMeter;
	}

	public void setStartTripGeoFenceAreaInMeter(int startTripGeoFenceAreaInMeter) {
		this.startTripGeoFenceAreaInMeter = startTripGeoFenceAreaInMeter;
	}

	public int getEndTripGeoFenceAreaInMeter() {
		return endTripGeoFenceAreaInMeter;
	}

	public void setEndTripGeoFenceAreaInMeter(int endTripGeoFenceAreaInMeter) {
		this.endTripGeoFenceAreaInMeter = endTripGeoFenceAreaInMeter;
	}

	public EFmFmClientMasterPO geteFmFmClientMaster() {
		return eFmFmClientMaster;
	}

	public void seteFmFmClientMaster(EFmFmClientMasterPO eFmFmClientMaster) {
		this.eFmFmClientMaster = eFmFmClientMaster;
	}

	public List<EFmFmAlertTypeMasterPO> getEfmFmAlertTypeMasters() {
		return efmFmAlertTypeMasters;
	}

	public void setEfmFmAlertTypeMasters(List<EFmFmAlertTypeMasterPO> efmFmAlertTypeMasters) {
		this.efmFmAlertTypeMasters = efmFmAlertTypeMasters;
	}

	public List<EFmFmClientRouteMappingPO> getEfmFmClientRouteMappings() {
		return efmFmClientRouteMappings;
	}

	public void setEfmFmClientRouteMappings(List<EFmFmClientRouteMappingPO> efmFmClientRouteMappings) {
		this.efmFmClientRouteMappings = efmFmClientRouteMappings;
	}

	public List<EFmFmClientUserRolePO> getEfmFmClientUserRoles() {
		return efmFmClientUserRoles;
	}

	public void setEfmFmClientUserRoles(List<EFmFmClientUserRolePO> efmFmClientUserRoles) {
		this.efmFmClientUserRoles = efmFmClientUserRoles;
	}

	public List<EFmFmTripTimingMasterPO> geteFmFmShiftTiming() {
		return eFmFmShiftTiming;
	}

	public void seteFmFmShiftTiming(List<EFmFmTripTimingMasterPO> eFmFmShiftTiming) {
		this.eFmFmShiftTiming = eFmFmShiftTiming;
	}

	public List<EFmFmUserMasterPO> getEfmFmUserMasters() {
		return efmFmUserMasters;
	}

	public void setEfmFmUserMasters(List<EFmFmUserMasterPO> efmFmUserMasters) {
		this.efmFmUserMasters = efmFmUserMasters;
	}

	public List<EFmFmVendorMasterPO> getEfmFmVendorMasters() {
		return efmFmVendorMasters;
	}

	public void setEfmFmVendorMasters(List<EFmFmVendorMasterPO> efmFmVendorMasters) {
		this.efmFmVendorMasters = efmFmVendorMasters;
	}

	public List<EFmFmAssignRoutePO> geteFmFmAssignRoute() {
		return eFmFmAssignRoute;
	}

	public void seteFmFmAssignRoute(List<EFmFmAssignRoutePO> eFmFmAssignRoute) {
		this.eFmFmAssignRoute = eFmFmAssignRoute;
	}

	public List<EFmFmClientProjectDetailsPO> geteFmFmClientProjectDetails() {
		return eFmFmClientProjectDetails;
	}

	public void seteFmFmClientProjectDetails(List<EFmFmClientProjectDetailsPO> eFmFmClientProjectDetails) {
		this.eFmFmClientProjectDetails = eFmFmClientProjectDetails;
	}

	public List<EFmFmActualRoutTravelledPO> geteFmFmActualRouteTravelled() {
		return eFmFmActualRouteTravelled;
	}

	public void seteFmFmActualRouteTravelled(List<EFmFmActualRoutTravelledPO> eFmFmActualRouteTravelled) {
		this.eFmFmActualRouteTravelled = eFmFmActualRouteTravelled;
	}

	public List<EFmFmVendorContractTypeMasterPO> geteFmFmVendorContractTypeMaster() {
		return eFmFmVendorContractTypeMaster;
	}

	public void seteFmFmVendorContractTypeMaster(List<EFmFmVendorContractTypeMasterPO> eFmFmVendorContractTypeMaster) {
		this.eFmFmVendorContractTypeMaster = eFmFmVendorContractTypeMaster;
	}

	public List<EFmFmDeviceMasterPO> geteFmFmDeviceMaster() {
		return eFmFmDeviceMaster;
	}

	public void seteFmFmDeviceMaster(List<EFmFmDeviceMasterPO> eFmFmDeviceMaster) {
		this.eFmFmDeviceMaster = eFmFmDeviceMaster;
	}

	public List<EFmFmClientBranchConfigurationMappingPO> geteFmFmClientBranchConfigurationMapping() {
		return eFmFmClientBranchConfigurationMapping;
	}

	public void seteFmFmClientBranchConfigurationMapping(
			List<EFmFmClientBranchConfigurationMappingPO> eFmFmClientBranchConfigurationMapping) {
		this.eFmFmClientBranchConfigurationMapping = eFmFmClientBranchConfigurationMapping;
	}

	public String getFeedBackEmailId() {
		return feedBackEmailId;
	}

	public void setFeedBackEmailId(String feedBackEmailId) {
		this.feedBackEmailId = feedBackEmailId;
	}

	public String getBranchDescription() {
		return branchDescription;
	}

	public void setBranchDescription(String branchDescription) {
		this.branchDescription = branchDescription;
	}

	public String getBranchUri() {
		return branchUri;
	}

	public void setBranchUri(String branchUri) {
		this.branchUri = branchUri;
	}

	public int getClusterSize() {
		return clusterSize;
	}

	public void setClusterSize(int clusterSize) {
		this.clusterSize = clusterSize;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public int getNumberOfAdministarator() {
		return numberOfAdministarator;
	}

	public void setNumberOfAdministarator(int numberOfAdministarator) {
		this.numberOfAdministarator = numberOfAdministarator;
	}

	public int getPasswordResetPeriodForAdminInDays() {
		return passwordResetPeriodForAdminInDays;
	}

	public void setPasswordResetPeriodForAdminInDays(int passwordResetPeriodForAdminInDays) {
		this.passwordResetPeriodForAdminInDays = passwordResetPeriodForAdminInDays;
	}

	public String getPanicAlertNeeded() {
		return panicAlertNeeded;
	}

	public Time getShiftTimeDiffPickToDrop() {
		return shiftTimeDiffPickToDrop;
	}

	public void setShiftTimeDiffPickToDrop(Time shiftTimeDiffPickToDrop) {
		this.shiftTimeDiffPickToDrop = shiftTimeDiffPickToDrop;
	}

	public int getAddingGeoFenceDistanceIntrip() {
		return addingGeoFenceDistanceIntrip;
	}

	public void setAddingGeoFenceDistanceIntrip(int addingGeoFenceDistanceIntrip) {
		this.addingGeoFenceDistanceIntrip = addingGeoFenceDistanceIntrip;
	}

	public int getEmployeeSecondPickUpRequest() {
		return employeeSecondPickUpRequest;
	}

	public void setEmployeeSecondPickUpRequest(int employeeSecondPickUpRequest) {
		this.employeeSecondPickUpRequest = employeeSecondPickUpRequest;
	}

	public int getEmployeeSecondDropRequest() {
		return employeeSecondDropRequest;
	}

	public void setEmployeeSecondDropRequest(int employeeSecondDropRequest) {
		this.employeeSecondDropRequest = employeeSecondDropRequest;
	}

	public void setPanicAlertNeeded(String panicAlertNeeded) {
		this.panicAlertNeeded = panicAlertNeeded;
	}

	public String getAutoDropRoster() {
		return autoDropRoster;
	}

	public void setAutoDropRoster(String autoDropRoster) {
		this.autoDropRoster = autoDropRoster;
	}

	public int getLastPassCanNotCurrentPass() {
		return lastPassCanNotCurrentPass;
	}

	public void setLastPassCanNotCurrentPass(int lastPassCanNotCurrentPass) {
		this.lastPassCanNotCurrentPass = lastPassCanNotCurrentPass;
	}

	public List<EFmFmUserPasswordPO> geteFmFmUserPassword() {
		return eFmFmUserPassword;
	}

	public void seteFmFmUserPassword(List<EFmFmUserPasswordPO> eFmFmUserPassword) {
		this.eFmFmUserPassword = eFmFmUserPassword;
	}

	public String getTwoFactorAuthenticationRequired() {
		return twoFactorAuthenticationRequired;
	}

	public void setTwoFactorAuthenticationRequired(String twoFactorAuthenticationRequired) {
		this.twoFactorAuthenticationRequired = twoFactorAuthenticationRequired;
	}

	public int getSessionTimeoutInMinutes() {
		return sessionTimeoutInMinutes;
	}

	public void setSessionTimeoutInMinutes(int sessionTimeoutInMinutes) {
		this.sessionTimeoutInMinutes = sessionTimeoutInMinutes;
	}

	public int getNumberOfAttemptsWrongPass() {
		return numberOfAttemptsWrongPass;
	}

	public void setNumberOfAttemptsWrongPass(int numberOfAttemptsWrongPass) {
		this.numberOfAttemptsWrongPass = numberOfAttemptsWrongPass;
	}

	public String getEmpDeviceDriverImage() {
		return empDeviceDriverImage;
	}

	public void setEmpDeviceDriverImage(String empDeviceDriverImage) {
		this.empDeviceDriverImage = empDeviceDriverImage;
	}

	public String getEmpDeviceDriverMobileNumber() {
		return empDeviceDriverMobileNumber;
	}

	public void setEmpDeviceDriverMobileNumber(String empDeviceDriverMobileNumber) {
		this.empDeviceDriverMobileNumber = empDeviceDriverMobileNumber;
	}

	public String getEmpDeviceDriverName() {
		return empDeviceDriverName;
	}

	public void setEmpDeviceDriverName(String empDeviceDriverName) {
		this.empDeviceDriverName = empDeviceDriverName;
	}

	public String getDriverDeviceAutoCallAndsms() {
		return driverDeviceAutoCallAndsms;
	}

	public void setDriverDeviceAutoCallAndsms(String driverDeviceAutoCallAndsms) {
		this.driverDeviceAutoCallAndsms = driverDeviceAutoCallAndsms;
	}

	public String getDriverDeviceDriverProfilePicture() {
		return driverDeviceDriverProfilePicture;
	}

	public void setDriverDeviceDriverProfilePicture(String driverDeviceDriverProfilePicture) {
		this.driverDeviceDriverProfilePicture = driverDeviceDriverProfilePicture;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public int getInvoiceNumberDigitRange() {
		return invoiceNumberDigitRange;
	}

	public void setInvoiceNumberDigitRange(int invoiceNumberDigitRange) {
		this.invoiceNumberDigitRange = invoiceNumberDigitRange;
	}

	public String getAdhocTimePickerForEmployee() {
		return adhocTimePickerForEmployee;
	}

	public void setAdhocTimePickerForEmployee(String adhocTimePickerForEmployee) {
		this.adhocTimePickerForEmployee = adhocTimePickerForEmployee;
	}

	/*
	 * public int getDropPriorTimePeriod() { return dropPriorTimePeriod; }
	 * 
	 * 
	 * 
	 * public void setDropPriorTimePeriod(int dropPriorTimePeriod) {
	 * this.dropPriorTimePeriod = dropPriorTimePeriod; }
	 * 
	 * 
	 * 
	 * public int getPickupPriorTimePeriod() { return pickupPriorTimePeriod; }
	 * 
	 * 
	 * 
	 * public void setPickupPriorTimePeriod(int pickupPriorTimePeriod) {
	 * this.pickupPriorTimePeriod = pickupPriorTimePeriod; }
	 */

	public int getPasswordResetPeriodForUserInDays() {
		return passwordResetPeriodForUserInDays;
	}

	public void setPasswordResetPeriodForUserInDays(int passwordResetPeriodForUserInDays) {
		this.passwordResetPeriodForUserInDays = passwordResetPeriodForUserInDays;
	}

	public int getInactiveAdminAccountAfterNumOfDays() {
		return inactiveAdminAccountAfterNumOfDays;
	}

	public void setInactiveAdminAccountAfterNumOfDays(int inactiveAdminAccountAfterNumOfDays) {
		this.inactiveAdminAccountAfterNumOfDays = inactiveAdminAccountAfterNumOfDays;
	}

	public String getCutOffTime() {
		return cutOffTime;
	}

	public void setCutOffTime(String cutOffTime) {
		this.cutOffTime = cutOffTime;
	}

	public List<EFmFmFuelChargesPO> geteFmFmFuelCharge() {
		return eFmFmFuelCharge;
	}

	public void seteFmFmFuelCharge(List<EFmFmFuelChargesPO> eFmFmFuelCharge) {
		this.eFmFmFuelCharge = eFmFmFuelCharge;
	}

	public int getInvoiceGenDate() {
		return invoiceGenDate;
	}

	public int getInvoiceNoOfDWorkingDays() {
		return invoiceNoOfDWorkingDays;
	}

	public void setInvoiceGenDate(int invoiceGenDate) {
		this.invoiceGenDate = invoiceGenDate;
	}

	public void setInvoiceNoOfDWorkingDays(int invoiceNoOfDWorkingDays) {
		this.invoiceNoOfDWorkingDays = invoiceNoOfDWorkingDays;
	}

	public Time getDropPriorTimePeriod() {
		return dropPriorTimePeriod;
	}

	public void setDropPriorTimePeriod(Time dropPriorTimePeriod) {
		this.dropPriorTimePeriod = dropPriorTimePeriod;
	}

	public Time getPickupPriorTimePeriod() {
		return pickupPriorTimePeriod;
	}

	public void setPickupPriorTimePeriod(Time pickupPriorTimePeriod) {
		this.pickupPriorTimePeriod = pickupPriorTimePeriod;
	}

	public String getDropCutOffTime() {
		return dropCutOffTime;
	}

	public void setDropCutOffTime(String dropCutOffTime) {
		this.dropCutOffTime = dropCutOffTime;
	}

	public String getPickupCutOffTime() {
		return pickupCutOffTime;
	}

	public void setPickupCutOffTime(String pickupCutOffTime) {
		this.pickupCutOffTime = pickupCutOffTime;
	}

	public String getDropPickupReqCutOfTime() {
		return dropPickupReqCutOfTime;
	}

	public void setDropPickupReqCutOfTime(String dropPickupReqCutOfTime) {
		this.dropPickupReqCutOfTime = dropPickupReqCutOfTime;
	}

	public String getDropCancelCutOffTime() {
		return dropCancelCutOffTime;
	}

	public void setDropCancelCutOffTime(String dropCancelCutOffTime) {
		this.dropCancelCutOffTime = dropCancelCutOffTime;
	}

	public String getPickupCancelCutOffTime() {
		return pickupCancelCutOffTime;
	}

	public void setPickupCancelCutOffTime(String pickupCancelCutOffTime) {
		this.pickupCancelCutOffTime = pickupCancelCutOffTime;
	}

	public Time getDropCancelTimePeriod() {
		return dropCancelTimePeriod;
	}

	public void setDropCancelTimePeriod(Time dropCancelTimePeriod) {
		this.dropCancelTimePeriod = dropCancelTimePeriod;
	}

	public Time getPickupCancelTimePeriod() {
		return pickupCancelTimePeriod;
	}

	public void setPickupCancelTimePeriod(Time pickupCancelTimePeriod) {
		this.pickupCancelTimePeriod = pickupCancelTimePeriod;
	}

	public String getDriverAutoCheckOut() {
		return driverAutoCheckOut;
	}

	public void setDriverAutoCheckOut(String driverAutoCheckOut) {
		this.driverAutoCheckOut = driverAutoCheckOut;
	}

	public String getReschedulePickupTime() {
		return reschedulePickupTime;
	}

	public void setReschedulePickupTime(String reschedulePickupTime) {
		this.reschedulePickupTime = reschedulePickupTime;
	}

	public String getRescheduleDropTime() {
		return rescheduleDropTime;
	}

	public void setRescheduleDropTime(String rescheduleDropTime) {
		this.rescheduleDropTime = rescheduleDropTime;
	}

	public Time getReschedulePickupCutOffTime() {
		return reschedulePickupCutOffTime;
	}

	public void setReschedulePickupCutOffTime(Time reschedulePickupCutOffTime) {
		this.reschedulePickupCutOffTime = reschedulePickupCutOffTime;
	}

	public Time getRescheduleDropCutOffTime() {
		return rescheduleDropCutOffTime;
	}

	public void setRescheduleDropCutOffTime(Time rescheduleDropCutOffTime) {
		this.rescheduleDropCutOffTime = rescheduleDropCutOffTime;
	}

	public List<EFmFmLiveRoutTravelledPO> geteFmFmLiveRoutTravelledPO() {
		return eFmFmLiveRoutTravelledPO;
	}

	public void seteFmFmLiveRoutTravelledPO(List<EFmFmLiveRoutTravelledPO> eFmFmLiveRoutTravelledPO) {
		this.eFmFmLiveRoutTravelledPO = eFmFmLiveRoutTravelledPO;
	}

	public List<EFmFmRoutingAreaPO> geteFmFmRoutingArea() {
		return eFmFmRoutingArea;
	}

	public void seteFmFmRoutingArea(List<EFmFmRoutingAreaPO> eFmFmRoutingArea) {
		this.eFmFmRoutingArea = eFmFmRoutingArea;
	}

	public List<AreaEmpClusterMappingPO> getAreaEmpClusterMapping() {
		return areaEmpClusterMapping;
	}

	public void setAreaEmpClusterMapping(List<AreaEmpClusterMappingPO> areaEmpClusterMapping) {
		this.areaEmpClusterMapping = areaEmpClusterMapping;
	}

	public List<EFmFmAlgoRoutesPO> geteFmFmAlgoRoutes() {
		return eFmFmAlgoRoutes;
	}

	public void seteFmFmAlgoRoutes(List<EFmFmAlgoRoutesPO> eFmFmAlgoRoutes) {
		this.eFmFmAlgoRoutes = eFmFmAlgoRoutes;
	}

	public int getAreaGeoFenceRegion() {
		return areaGeoFenceRegion;
	}

	public void setAreaGeoFenceRegion(int areaGeoFenceRegion) {
		this.areaGeoFenceRegion = areaGeoFenceRegion;
	}

	public int getClusterGeoFenceRegion() {
		return clusterGeoFenceRegion;
	}

	public void setClusterGeoFenceRegion(int clusterGeoFenceRegion) {
		this.clusterGeoFenceRegion = clusterGeoFenceRegion;
	}

	public int getRouteGeoFenceRegion() {
		return routeGeoFenceRegion;
	}

	public void setRouteGeoFenceRegion(int routeGeoFenceRegion) {
		this.routeGeoFenceRegion = routeGeoFenceRegion;
	}

	public String getShiftTime() {
		return shiftTime;
	}

	public void setShiftTime(String shiftTime) {
		this.shiftTime = shiftTime;
	}

	public String getShiftSelectType() {
		return shiftSelectType;
	}

	public void setShiftSelectType(String shiftSelectType) {
		this.shiftSelectType = shiftSelectType;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDistanceFlg() {
		return distanceFlg;
	}

	public void setDistanceFlg(String distanceFlg) {
		this.distanceFlg = distanceFlg;
	}

	public String getDriverAutoCheckoutStatus() {
		return driverAutoCheckoutStatus;
	}

	public void setDriverAutoCheckoutStatus(String driverAutoCheckoutStatus) {
		this.driverAutoCheckoutStatus = driverAutoCheckoutStatus;
	}

	public String getAutoVehicleAllocationStatus() {
		return autoVehicleAllocationStatus;
	}

	public void setAutoVehicleAllocationStatus(String autoVehicleAllocationStatus) {
		this.autoVehicleAllocationStatus = autoVehicleAllocationStatus;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public Date getRequestCutOffDate() {
		return requestCutOffDate;
	}

	public void setRequestCutOffDate(Date requestCutOffDate) {
		this.requestCutOffDate = requestCutOffDate;
	}

	public int getRequestCutOffNoOfDays() {
		return requestCutOffNoOfDays;
	}

	public void setRequestCutOffNoOfDays(int requestCutOffNoOfDays) {
		this.requestCutOffNoOfDays = requestCutOffNoOfDays;
	}

	

	public int getEarlyRequestDate() {
		return earlyRequestDate;
	}

	public void setEarlyRequestDate(int earlyRequestDate) {
		this.earlyRequestDate = earlyRequestDate;
	}

	public String getOccurrenceFlg() {
		return occurrenceFlg;
	}

	public void setOccurrenceFlg(String occurrenceFlg) {
		this.occurrenceFlg = occurrenceFlg;
	}

	public String getMonthOrDays() {
		return monthOrDays;
	}

	public void setMonthOrDays(String monthOrDays) {
		this.monthOrDays = monthOrDays;
	}

	public String getDaysRequest() {
		return daysRequest;
	}

	public void setDaysRequest(String daysRequest) {
		this.daysRequest = daysRequest;
	}

	public String getRequestFromDateCutOff() {
		return requestFromDateCutOff;
	}

	public void setRequestFromDateCutOff(String requestFromDateCutOff) {
		this.requestFromDateCutOff = requestFromDateCutOff;
	}

	public Date getRequestCutOffFromDate() {
		return requestCutOffFromDate;
	}

	public void setRequestCutOffFromDate(Date requestCutOffFromDate) {
		this.requestCutOffFromDate = requestCutOffFromDate;
	}

	public String getRequestToDateCutOff() {
		return requestToDateCutOff;
	}

	public void setRequestToDateCutOff(String requestToDateCutOff) {
		this.requestToDateCutOff = requestToDateCutOff;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<EFmFmFieldAppConfigMasterPO> geteFmFmFieldAppConfigMaster() {
		return eFmFmFieldAppConfigMaster;
	}

	public void seteFmFmFieldAppConfigMaster(List<EFmFmFieldAppConfigMasterPO> eFmFmFieldAppConfigMaster) {
		this.eFmFmFieldAppConfigMaster = eFmFmFieldAppConfigMaster;
	}

	public String getInvoiceGenType() {
		return invoiceGenType;
	}

	public void setInvoiceGenType(String invoiceGenType) {
		this.invoiceGenType = invoiceGenType;
	}

	public String getLocationVisible() {
		return locationVisible;
	}

	public void setLocationVisible(String locationVisible) {
		this.locationVisible = locationVisible;
	}

	public int getDestinationPointLimit() {
		return destinationPointLimit;
	}

	public void setDestinationPointLimit(int destinationPointLimit) {
		this.destinationPointLimit = destinationPointLimit;
	}

/*	public String getTravelTimeFromDropToFirstPickUp() {
		return travelTimeFromDropToFirstPickUp;
	}

	public void setTravelTimeFromDropToFirstPickUp(String travelTimeFromDropToFirstPickUp) {
		this.travelTimeFromDropToFirstPickUp = travelTimeFromDropToFirstPickUp;
	}

	public Time getDriverWaitingTimeAtLastLocation() {
		return driverWaitingTimeAtLastLocation;
	}

	public void setDriverWaitingTimeAtLastLocation(Time driverWaitingTimeAtLastLocation) {
		this.driverWaitingTimeAtLastLocation = driverWaitingTimeAtLastLocation;
	}*/

	public String getSelectedB2bType() {
		return selectedB2bType;
	}

	public void setSelectedB2bType(String selectedB2bType) {
		this.selectedB2bType = selectedB2bType;
	}

	
	
	public double getB2bByTravelDistanceInKM() {
		return b2bByTravelDistanceInKM;
	}

	public void setB2bByTravelDistanceInKM(double b2bByTravelDistanceInKM) {
		this.b2bByTravelDistanceInKM = b2bByTravelDistanceInKM;
	}

	public Time getDriverWaitingTimeAtLastLocation() {
		return driverWaitingTimeAtLastLocation;
	}

	public void setDriverWaitingTimeAtLastLocation(Time driverWaitingTimeAtLastLocation) {
		this.driverWaitingTimeAtLastLocation = driverWaitingTimeAtLastLocation;
	}

	public Time getB2bByTravelTime() {
		return b2bByTravelTime;
	}

	public void setB2bByTravelTime(Time b2bByTravelTime) {
		this.b2bByTravelTime = b2bByTravelTime;
	}

	public String getDriverLastLocWaitingTime() {
		return driverLastLocWaitingTime;
	}

	public void setDriverLastLocWaitingTime(String driverLastLocWaitingTime) {
		this.driverLastLocWaitingTime = driverLastLocWaitingTime;
	}

	public String getTravelTimeFromDropToFirstPickUp() {
		return travelTimeFromDropToFirstPickUp;
	}

	public void setTravelTimeFromDropToFirstPickUp(String travelTimeFromDropToFirstPickUp) {
		this.travelTimeFromDropToFirstPickUp = travelTimeFromDropToFirstPickUp;
	}

	public String getNotificationCutoffTimePickup() {
		return notificationCutoffTimePickup;
	}

	public void setNotificationCutoffTimePickup(String notificationCutoffTimePickup) {
		this.notificationCutoffTimePickup = notificationCutoffTimePickup;
	}

	public String getNotificationCutoffTimeDrop() {
		return notificationCutoffTimeDrop;
	}

	public void setNotificationCutoffTimeDrop(String notificationCutoffTimeDrop) {
		this.notificationCutoffTimeDrop = notificationCutoffTimeDrop;
	}

	public String getPersonalDeviceViaSms() {
		return personalDeviceViaSms;
	}

	public void setPersonalDeviceViaSms(String personalDeviceViaSms) {
		this.personalDeviceViaSms = personalDeviceViaSms;
	}

	public String getAssignRoutesToVendor() {
		return assignRoutesToVendor;
	}

	public void setAssignRoutesToVendor(String assignRoutesToVendor) {
		this.assignRoutesToVendor = assignRoutesToVendor;
	}

	public String getDriverCallToEmployee() {
		return driverCallToEmployee;
	}

	public void setDriverCallToEmployee(String driverCallToEmployee) {
		this.driverCallToEmployee = driverCallToEmployee;
	}

	public String getDriverCallToTransportDesk() {
		return driverCallToTransportDesk;
	}

	public void setDriverCallToTransportDesk(String driverCallToTransportDesk) {
		this.driverCallToTransportDesk = driverCallToTransportDesk;
	}

	public String getEmployeeCallToDriver() {
		return employeeCallToDriver;
	}

	public void setEmployeeCallToDriver(String employeeCallToDriver) {
		this.employeeCallToDriver = employeeCallToDriver;
	}

	public String getEmployeeCallToTransport() {
		return employeeCallToTransport;
	}

	public void setEmployeeCallToTransport(String employeeCallToTransport) {
		this.employeeCallToTransport = employeeCallToTransport;
	}

	public Time getNotificationCutoffTimeForPickup() {
		return notificationCutoffTimeForPickup;
	}

	public void setNotificationCutoffTimeForPickup(Time notificationCutoffTimeForPickup) {
		this.notificationCutoffTimeForPickup = notificationCutoffTimeForPickup;
	}

	public Time getNotificationCutoffTimeForDrop() {
		return notificationCutoffTimeForDrop;
	}

	public void setNotificationCutoffTimeForDrop(Time notificationCutoffTimeForDrop) {
		this.notificationCutoffTimeForDrop = notificationCutoffTimeForDrop;
	}

	public String getSecondEmergencyContactNumber() {
		return secondEmergencyContactNumber;
	}

	public void setSecondEmergencyContactNumber(String secondEmergencyContactNumber) {
		this.secondEmergencyContactNumber = secondEmergencyContactNumber;
	}

	public int getLicenseExpiryDay() {
		return licenseExpiryDay;
	}

	public void setLicenseExpiryDay(int licenseExpiryDay) {
		this.licenseExpiryDay = licenseExpiryDay;
	}

	public int getLicenseRepeatAlertsEvery() {
		return licenseRepeatAlertsEvery;
	}

	public void setLicenseRepeatAlertsEvery(int licenseRepeatAlertsEvery) {
		this.licenseRepeatAlertsEvery = licenseRepeatAlertsEvery;
	}

	public String getLicenceNotificationType() {
		return licenceNotificationType;
	}

	public void setLicenceNotificationType(String licenceNotificationType) {
		this.licenceNotificationType = licenceNotificationType;
	}

	public String getLicenseSMSNumber() {
		return licenseSMSNumber;
	}

	public void setLicenseSMSNumber(String licenseSMSNumber) {
		this.licenseSMSNumber = licenseSMSNumber;
	}

	public String getLicenseEmailId() {
		return licenseEmailId;
	}

	public void setLicenseEmailId(String licenseEmailId) {
		this.licenseEmailId = licenseEmailId;
	}

	public int getMedicalFitnessExpiryDay() {
		return medicalFitnessExpiryDay;
	}

	public void setMedicalFitnessExpiryDay(int medicalFitnessExpiryDay) {
		this.medicalFitnessExpiryDay = medicalFitnessExpiryDay;
	}

	public int getMedicalFitnessRepeatAlertsEvery() {
		return medicalFitnessRepeatAlertsEvery;
	}

	public void setMedicalFitnessRepeatAlertsEvery(int medicalFitnessRepeatAlertsEvery) {
		this.medicalFitnessRepeatAlertsEvery = medicalFitnessRepeatAlertsEvery;
	}

	public String getMedicalFitnessNotificationType() {
		return medicalFitnessNotificationType;
	}

	public void setMedicalFitnessNotificationType(String medicalFitnessNotificationType) {
		this.medicalFitnessNotificationType = medicalFitnessNotificationType;
	}

	public String getMedicalFitnessSMSNumber() {
		return medicalFitnessSMSNumber;
	}

	public void setMedicalFitnessSMSNumber(String medicalFitnessSMSNumber) {
		this.medicalFitnessSMSNumber = medicalFitnessSMSNumber;
	}

	public String getMedicalFitnessEmailId() {
		return medicalFitnessEmailId;
	}

	public void setMedicalFitnessEmailId(String medicalFitnessEmailId) {
		this.medicalFitnessEmailId = medicalFitnessEmailId;
	}

	public int getPoliceVerificationExpiryDay() {
		return policeVerificationExpiryDay;
	}

	public void setPoliceVerificationExpiryDay(int policeVerificationExpiryDay) {
		this.policeVerificationExpiryDay = policeVerificationExpiryDay;
	}

	public int getPoliceVerificationRepeatAlertsEvery() {
		return policeVerificationRepeatAlertsEvery;
	}

	public void setPoliceVerificationRepeatAlertsEvery(int policeVerificationRepeatAlertsEvery) {
		this.policeVerificationRepeatAlertsEvery = policeVerificationRepeatAlertsEvery;
	}

	public String getPoliceVerificationNotificationType() {
		return policeVerificationNotificationType;
	}

	public void setPoliceVerificationNotificationType(String policeVerificationNotificationType) {
		this.policeVerificationNotificationType = policeVerificationNotificationType;
	}

	public String getPoliceVerificationSMSNumber() {
		return policeVerificationSMSNumber;
	}

	public void setPoliceVerificationSMSNumber(String policeVerificationSMSNumber) {
		this.policeVerificationSMSNumber = policeVerificationSMSNumber;
	}

	public String getPoliceVerificationEmailId() {
		return policeVerificationEmailId;
	}

	public void setPoliceVerificationEmailId(String policeVerificationEmailId) {
		this.policeVerificationEmailId = policeVerificationEmailId;
	}

	public String getDdTrainingNotificationType() {
		return ddTrainingNotificationType;
	}

	public void setDdTrainingNotificationType(String ddTrainingNotificationType) {
		this.ddTrainingNotificationType = ddTrainingNotificationType;
	}
	
	public int getDdTrainingExpiryDay() {
		return ddTrainingExpiryDay;
	}

	public void setDdTrainingExpiryDay(int ddTrainingExpiryDay) {
		this.ddTrainingExpiryDay = ddTrainingExpiryDay;
	}

	public int getDdTrainingRepeatAlertsEvery() {
		return ddTrainingRepeatAlertsEvery;
	}

	public void setDdTrainingRepeatAlertsEvery(int ddTrainingRepeatAlertsEvery) {
		this.ddTrainingRepeatAlertsEvery = ddTrainingRepeatAlertsEvery;
	}

	public String getDdTrainingSMSNumber() {
		return ddTrainingSMSNumber;
	}

	public void setDdTrainingSMSNumber(String ddTrainingSMSNumber) {
		this.ddTrainingSMSNumber = ddTrainingSMSNumber;
	}

	public String getDdTrainingEmailId() {
		return ddTrainingEmailId;
	}

	public void setDdTrainingEmailId(String ddTrainingEmailId) {
		this.ddTrainingEmailId = ddTrainingEmailId;
	}

	public int getPollutionDueExpiryDay() {
		return pollutionDueExpiryDay;
	}

	public void setPollutionDueExpiryDay(int pollutionDueExpiryDay) {
		this.pollutionDueExpiryDay = pollutionDueExpiryDay;
	}

	public int getPollutionDueRepeatAlertsEvery() {
		return pollutionDueRepeatAlertsEvery;
	}

	public void setPollutionDueRepeatAlertsEvery(int pollutionDueRepeatAlertsEvery) {
		this.pollutionDueRepeatAlertsEvery = pollutionDueRepeatAlertsEvery;
	}

	public String getPollutionDueNotificationType() {
		return pollutionDueNotificationType;
	}

	public void setPollutionDueNotificationType(String pollutionDueNotificationType) {
		this.pollutionDueNotificationType = pollutionDueNotificationType;
	}

	public String getPollutionDueSMSNumber() {
		return pollutionDueSMSNumber;
	}

	public void setPollutionDueSMSNumber(String pollutionDueSMSNumber) {
		this.pollutionDueSMSNumber = pollutionDueSMSNumber;
	}

	public String getPollutionDueEmailId() {
		return pollutionDueEmailId;
	}

	public void setPollutionDueEmailId(String pollutionDueEmailId) {
		this.pollutionDueEmailId = pollutionDueEmailId;
	}

	public int getInsuranceDueExpiryDay() {
		return insuranceDueExpiryDay;
	}

	public void setInsuranceDueExpiryDay(int insuranceDueExpiryDay) {
		this.insuranceDueExpiryDay = insuranceDueExpiryDay;
	}

	public int getInsuranceDueRepeatAlertsEvery() {
		return insuranceDueRepeatAlertsEvery;
	}

	public void setInsuranceDueRepeatAlertsEvery(int insuranceDueRepeatAlertsEvery) {
		this.insuranceDueRepeatAlertsEvery = insuranceDueRepeatAlertsEvery;
	}

	public String getInsuranceDueNotificationType() {
		return insuranceDueNotificationType;
	}

	public void setInsuranceDueNotificationType(String insuranceDueNotificationType) {
		this.insuranceDueNotificationType = insuranceDueNotificationType;
	}

	public String getInsuranceDueSMSNumber() {
		return insuranceDueSMSNumber;
	}

	public void setInsuranceDueSMSNumber(String insuranceDueSMSNumber) {
		this.insuranceDueSMSNumber = insuranceDueSMSNumber;
	}

	public String getInsuranceDueEmailId() {
		return insuranceDueEmailId;
	}

	public void setInsuranceDueEmailId(String insuranceDueEmailId) {
		this.insuranceDueEmailId = insuranceDueEmailId;
	}

	public int getTaxCertificateExpiryDay() {
		return taxCertificateExpiryDay;
	}

	public void setTaxCertificateExpiryDay(int taxCertificateExpiryDay) {
		this.taxCertificateExpiryDay = taxCertificateExpiryDay;
	}

	public int getTaxCertificateRepeatAlertsEvery() {
		return taxCertificateRepeatAlertsEvery;
	}

	public void setTaxCertificateRepeatAlertsEvery(int taxCertificateRepeatAlertsEvery) {
		this.taxCertificateRepeatAlertsEvery = taxCertificateRepeatAlertsEvery;
	}

	public String getTaxCertificateNotificationType() {
		return taxCertificateNotificationType;
	}

	public void setTaxCertificateNotificationType(String taxCertificateNotificationType) {
		this.taxCertificateNotificationType = taxCertificateNotificationType;
	}

	public String getTaxCertificateSMSNumber() {
		return taxCertificateSMSNumber;
	}

	public void setTaxCertificateSMSNumber(String taxCertificateSMSNumber) {
		this.taxCertificateSMSNumber = taxCertificateSMSNumber;
	}

	public String getTaxCertificateEmailId() {
		return taxCertificateEmailId;
	}

	public void setTaxCertificateEmailId(String taxCertificateEmailId) {
		this.taxCertificateEmailId = taxCertificateEmailId;
	}

	public int getPermitDueExpiryDay() {
		return permitDueExpiryDay;
	}

	public void setPermitDueExpiryDay(int permitDueExpiryDay) {
		this.permitDueExpiryDay = permitDueExpiryDay;
	}

	public int getPermitDueRepeatAlertsEvery() {
		return permitDueRepeatAlertsEvery;
	}

	public void setPermitDueRepeatAlertsEvery(int permitDueRepeatAlertsEvery) {
		this.permitDueRepeatAlertsEvery = permitDueRepeatAlertsEvery;
	}

	public String getPermitDueNotificationType() {
		return permitDueNotificationType;
	}

	public void setPermitDueNotificationType(String permitDueNotificationType) {
		this.permitDueNotificationType = permitDueNotificationType;
	}

	public String getPermitDueSMSNumber() {
		return permitDueSMSNumber;
	}

	public void setPermitDueSMSNumber(String permitDueSMSNumber) {
		this.permitDueSMSNumber = permitDueSMSNumber;
	}

	public String getPermitDueEmailId() {
		return permitDueEmailId;
	}

	public void setPermitDueEmailId(String permitDueEmailId) {
		this.permitDueEmailId = permitDueEmailId;
	}

	public int getVehicelMaintenanceExpiryDay() {
		return vehicelMaintenanceExpiryDay;
	}

	public void setVehicelMaintenanceExpiryDay(int vehicelMaintenanceExpiryDay) {
		this.vehicelMaintenanceExpiryDay = vehicelMaintenanceExpiryDay;
	}

	public int getVehicelMaintenanceRepeatAlertsEvery() {
		return vehicelMaintenanceRepeatAlertsEvery;
	}

	public void setVehicelMaintenanceRepeatAlertsEvery(int vehicelMaintenanceRepeatAlertsEvery) {
		this.vehicelMaintenanceRepeatAlertsEvery = vehicelMaintenanceRepeatAlertsEvery;
	}

	public String getVehicelMaintenanceNotificationType() {
		return vehicelMaintenanceNotificationType;
	}

	public void setVehicelMaintenanceNotificationType(String vehicelMaintenanceNotificationType) {
		this.vehicelMaintenanceNotificationType = vehicelMaintenanceNotificationType;
	}

	public String getVehicelMaintenanceSMSNumber() {
		return vehicelMaintenanceSMSNumber;
	}

	public void setVehicelMaintenanceSMSNumber(String vehicelMaintenanceSMSNumber) {
		this.vehicelMaintenanceSMSNumber = vehicelMaintenanceSMSNumber;
	}

	public String getVehicelMaintenanceEmailId() {
		return vehicelMaintenanceEmailId;
	}

	public void setVehicelMaintenanceEmailId(String vehicelMaintenanceEmailId) {
		this.vehicelMaintenanceEmailId = vehicelMaintenanceEmailId;
	}

	public String getEscortTimeStatus() {
		return escortTimeStatus;
	}

	public void setEscortTimeStatus(String escortTimeStatus) {
		this.escortTimeStatus = escortTimeStatus;
	}



	public int getSessionNotificationTime() {
		return sessionNotificationTime;
	}

	public void setSessionNotificationTime(int sessionNotificationTime) {
		this.sessionNotificationTime = sessionNotificationTime;
	}

	public Time getDissableTimeOTP() {
		return dissableTimeOTP;
	}

	public void setDissableTimeOTP(Time dissableTimeOTP) {
		this.dissableTimeOTP = dissableTimeOTP;
	}

	public int getMaxTimeOTP() {
		return maxTimeOTP;
	}

	public void setMaxTimeOTP(int maxTimeOTP) {
		this.maxTimeOTP = maxTimeOTP;
	}

	public String getOtpDisableTime() {
		return otpDisableTime;
	}

	public void setOtpDisableTime(String otpDisableTime) {
		this.otpDisableTime = otpDisableTime;
	}

	public int getImageUploadSize() {
		return imageUploadSize;
	}

	public void setImageUploadSize(int imageUploadSize) {
		this.imageUploadSize = imageUploadSize;
	}

	public String getAuthorizationToken() {
		return authorizationToken;
	}

	public void setAuthorizationToken(String authorizationToken) {
		this.authorizationToken = authorizationToken;
	}

	public Date getTakenGenrationTime() {
		return takenGenrationTime;
	}

	public void setTakenGenrationTime(Date takenGenrationTime) {
		this.takenGenrationTime = takenGenrationTime;
	}

	public String getShiftTimeGenderPreference() {
		return shiftTimeGenderPreference;
	}

	public void setShiftTimeGenderPreference(String shiftTimeGenderPreference) {
		this.shiftTimeGenderPreference = shiftTimeGenderPreference;
	}

	public String getApprovalProcess() {
		return approvalProcess;
	}

	public void setApprovalProcess(String approvalProcess) {
		this.approvalProcess = approvalProcess;
	}

	public int getPostApproval() {
		return postApproval;
	}

	public void setPostApproval(int postApproval) {
		this.postApproval = postApproval;
	}

	public String getRequestWithProject() {
		return requestWithProject;
	}

	public void setRequestWithProject(String requestWithProject) {
		this.requestWithProject = requestWithProject;
	}

	public String getVehiceCheckList() {
		return vehiceCheckList;
	}

	public void setVehiceCheckList(String vehiceCheckList) {
		this.vehiceCheckList = vehiceCheckList;
	}

	public String getDriverTripHistory() {
		return driverTripHistory;
	}

	public void setDriverTripHistory(String driverTripHistory) {
		this.driverTripHistory = driverTripHistory;
	}

	public int getMinimumDestCount() {
		return minimumDestCount;
	}

	public void setMinimumDestCount(int minimumDestCount) {
		this.minimumDestCount = minimumDestCount;
	}

	public List<EFmFmDynamicVehicleCheckListPO> geteFmFmDynamicVehicleCheckListPO() {
		return eFmFmDynamicVehicleCheckListPO;
	}

	public void seteFmFmDynamicVehicleCheckListPO(List<EFmFmDynamicVehicleCheckListPO> eFmFmDynamicVehicleCheckListPO) {
		this.eFmFmDynamicVehicleCheckListPO = eFmFmDynamicVehicleCheckListPO;
	}

	public List<EFmFmSmsAlertMasterPO> geteFmFmSmsAlertMaster() {
		return eFmFmSmsAlertMaster;
	}

	public void seteFmFmSmsAlertMaster(List<EFmFmSmsAlertMasterPO> eFmFmSmsAlertMaster) {
		this.eFmFmSmsAlertMaster = eFmFmSmsAlertMaster;
	}

	public String getGpsKmModification() {
		return gpsKmModification;
	}

	public void setGpsKmModification(String gpsKmModification) {
		this.gpsKmModification = gpsKmModification;
	}

	public String getManagerReqCreateProcess() {
		return managerReqCreateProcess;
	}

	public void setManagerReqCreateProcess(String managerReqCreateProcess) {
		this.managerReqCreateProcess = managerReqCreateProcess;
	}

	public String getPlaCardPrint() {
		return plaCardPrint;
	}

	public void setPlaCardPrint(String plaCardPrint) {
		this.plaCardPrint = plaCardPrint;
	}

	public String getTranportCommunicationMailId() {
		return tranportCommunicationMailId;
	}

	public void setTranportCommunicationMailId(String tranportCommunicationMailId) {
		this.tranportCommunicationMailId = tranportCommunicationMailId;
	}
	
		public String getOnPickUpNoShowCancelDrop() {
		return onPickUpNoShowCancelDrop;
	}

	public void setOnPickUpNoShowCancelDrop(String onPickUpNoShowCancelDrop) {
		this.onPickUpNoShowCancelDrop = onPickUpNoShowCancelDrop;
	}

	public int getNumberOfConsecutiveNoShow() {
		return numberOfConsecutiveNoShow;
	}

	public void setNumberOfConsecutiveNoShow(int numberOfConsecutiveNoShow) {
		this.numberOfConsecutiveNoShow = numberOfConsecutiveNoShow;
	}

	public Time getTripConsiderDelayAfter() {
		return tripConsiderDelayAfter;
	}

	public void setTripConsiderDelayAfter(Time tripConsiderDelayAfter) {
		this.tripConsiderDelayAfter = tripConsiderDelayAfter;
	}

	public String getTripDelayTime() {
		return tripDelayTime;
	}

	public void setTripDelayTime(String tripDelayTime) {
		this.tripDelayTime = tripDelayTime;
	}

	public String getEmployeeChecKInVia() {
		return employeeChecKInVia;
	}

	public void setEmployeeChecKInVia(String employeeChecKInVia) {
		this.employeeChecKInVia = employeeChecKInVia;
	}

	public String getEmployeeFeedbackEmail() {
		return employeeFeedbackEmail;
	}

	public void setEmployeeFeedbackEmail(String employeeFeedbackEmail) {
		this.employeeFeedbackEmail = employeeFeedbackEmail;
	}

	public String getEmployeeFeedbackEmailId() {
		return employeeFeedbackEmailId;
	}

	public void setEmployeeFeedbackEmailId(String employeeFeedbackEmailId) {
		this.employeeFeedbackEmailId = employeeFeedbackEmailId;
	}

	public String getEmployeeAppReportBug() {
		return employeeAppReportBug;
	}

	public void setEmployeeAppReportBug(String employeeAppReportBug) {
		this.employeeAppReportBug = employeeAppReportBug;
	}

	public String getReportBugEmailIds() {
		return reportBugEmailIds;
	}

	public void setReportBugEmailIds(String reportBugEmailIds) {
		this.reportBugEmailIds = reportBugEmailIds;
	}

	public int getEmployeeCheckOutGeofenceRegion() {
		return employeeCheckOutGeofenceRegion;
	}

	public void setEmployeeCheckOutGeofenceRegion(int employeeCheckOutGeofenceRegion) {
		this.employeeCheckOutGeofenceRegion = employeeCheckOutGeofenceRegion;
	}

	public String getToEmployeeFeedBackEmail() {
		return toEmployeeFeedBackEmail;
	}

	public void setToEmployeeFeedBackEmail(String toEmployeeFeedBackEmail) {
		this.toEmployeeFeedBackEmail = toEmployeeFeedBackEmail;
	}
		public String getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(String userRoles) {
		this.userRoles = userRoles;
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

	public List<EFmFmFacilityToFacilityMappingPO> geteFmFmFacilityToFacilityMapping() {
		return eFmFmFacilityToFacilityMapping;
	}

	public void seteFmFmFacilityToFacilityMapping(List<EFmFmFacilityToFacilityMappingPO> eFmFmFacilityToFacilityMapping) {
		this.eFmFmFacilityToFacilityMapping = eFmFmFacilityToFacilityMapping;
	}

	public List<EFmFmUserFacilityMappingPO> geteFmFmUserFacilityMapping() {
		return eFmFmUserFacilityMapping;
	}

	public void seteFmFmUserFacilityMapping(List<EFmFmUserFacilityMappingPO> eFmFmUserFacilityMapping) {
		this.eFmFmUserFacilityMapping = eFmFmUserFacilityMapping;
	}

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}

	public List<EFmFmVehicleCapacityMasterPO> geteFmFmVehicleCapacityMaster() {
		return eFmFmVehicleCapacityMaster;
	}

	public void seteFmFmVehicleCapacityMaster(List<EFmFmVehicleCapacityMasterPO> eFmFmVehicleCapacityMaster) {
		this.eFmFmVehicleCapacityMaster = eFmFmVehicleCapacityMaster;
	}

	public List<EFmFmEmployeeTravelRequestPO> geteFmFmEmployeeTravelRequest() {
		return eFmFmEmployeeTravelRequest;
	}

	public void seteFmFmEmployeeTravelRequest(List<EFmFmEmployeeTravelRequestPO> eFmFmEmployeeTravelRequest) {
		this.eFmFmEmployeeTravelRequest = eFmFmEmployeeTravelRequest;
	}

	public List<EFmFmEmployeeRequestMasterPO> geteFmFmEmployeeRequestMaster() {
		return eFmFmEmployeeRequestMaster;
	}

	public void seteFmFmEmployeeRequestMaster(List<EFmFmEmployeeRequestMasterPO> eFmFmEmployeeRequestMaster) {
		this.eFmFmEmployeeRequestMaster = eFmFmEmployeeRequestMaster;
	}
	

	public String getEscortTimeWindowEnable() {
		return escortTimeWindowEnable;
	}

	public void setEscortTimeWindowEnable(String escortTimeWindowEnable) {
		this.escortTimeWindowEnable = escortTimeWindowEnable;
	}

	public Time getEscortStartTimePickup() {
		return escortStartTimePickup;
	}

	public void setEscortStartTimePickup(Time escortStartTimePickup) {
		this.escortStartTimePickup = escortStartTimePickup;
	}

	public String getEscortStartTimeForPickup() {
		return escortStartTimeForPickup;
	}

	public void setEscortStartTimeForPickup(String escortStartTimeForPickup) {
		this.escortStartTimeForPickup = escortStartTimeForPickup;
	}

	public Time getEscortEndTimePickup() {
		return escortEndTimePickup;
	}

	public void setEscortEndTimePickup(Time escortEndTimePickup) {
		this.escortEndTimePickup = escortEndTimePickup;
	}

	public Time getEscortStartTimeDrop() {
		return escortStartTimeDrop;
	}

	public void setEscortStartTimeDrop(Time escortStartTimeDrop) {
		this.escortStartTimeDrop = escortStartTimeDrop;
	}

	public Time getEscortEndTimeDrop() {
		return escortEndTimeDrop;
	}

	public void setEscortEndTimeDrop(Time escortEndTimeDrop) {
		this.escortEndTimeDrop = escortEndTimeDrop;
	}

	public String getEscortEndTimeForPickup() {
		return escortEndTimeForPickup;
	}

	public void setEscortEndTimeForPickup(String escortEndTimeForPickup) {
		this.escortEndTimeForPickup = escortEndTimeForPickup;
	}

	public String getEscortStartTimeForDrop() {
		return escortStartTimeForDrop;
	}

	public void setEscortStartTimeForDrop(String escortStartTimeForDrop) {
		this.escortStartTimeForDrop = escortStartTimeForDrop;
	}

	public String getEscortEndTimeForDrop() {
		return escortEndTimeForDrop;
	}

	public void setEscortEndTimeForDrop(String escortEndTimeForDrop) {
		this.escortEndTimeForDrop = escortEndTimeForDrop;
	}

	public boolean isMultiFacility() {
		return multiFacility;
	}

	public void setMultiFacility(boolean multiFacility) {
		this.multiFacility = multiFacility;
	}

	public String getMobileLoginVia() {
		return mobileLoginVia;
	}

	public String getMobileLoginUrl() {
		return mobileLoginUrl;
	}

	public void setMobileLoginVia(String mobileLoginVia) {
		this.mobileLoginVia = mobileLoginVia;
	}

	public void setMobileLoginUrl(String mobileLoginUrl) {
		this.mobileLoginUrl = mobileLoginUrl;
	}

	public String getSsoLoginUrl() {
		return ssoLoginUrl;
	}

	public void setSsoLoginUrl(String ssoLoginUrl) {
		this.ssoLoginUrl = ssoLoginUrl;
	}

	public int getMobilePageCount() {
		return mobilePageCount;
	}

	public void setMobilePageCount(int mobilePageCount) {
		this.mobilePageCount = mobilePageCount;
	}

	public int getWebPageCount() {
		return webPageCount;
	}

	public void setWebPageCount(int webPageCount) {
		this.webPageCount = webPageCount;
	}

	public String getGeoCodedAddress() {
		return geoCodedAddress;
	}

	public void setGeoCodedAddress(String geoCodedAddress) {
		this.geoCodedAddress = geoCodedAddress;
	}

	
	
}