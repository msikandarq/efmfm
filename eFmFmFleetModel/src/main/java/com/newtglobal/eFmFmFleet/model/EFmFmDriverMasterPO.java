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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * The persistent class for the eFmFmDriverMaster database table.
 * 
 */
@Entity
@Table(name="eFmFmDriverMaster")
public class EFmFmDriverMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="DriverId", length=10)
	private int driverId;
	
	@Column(name="StateName", length=50)
	private String stateName;

	@Column(name="CityName", length=50)
	private String cityName;
	
	@Column(name="Pincode", length=10)
	private int pinCode;

	@Column(name="Address", length=200)
	private String address;
	
	@Column(name="PermanentAddress", length=200)
	private String permanentAddress;
	

	@Column(name="CreatedBy", length=50)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreationTime", length=30)
	private Date creationTime;


	@Column(name="FirstName", length=50)
	private String firstName;
	
	@Column(name="MiddleName", length=50)
	private String middleName;

	@Column(name="LastName", length=50)
	private String lastName;


	@Column(name="FatherName", length=50)
	private String fatherName;
	
	@Column(name="ProfilePicPath", length=250)
	private String profilePicPath;

	@Column(name="Feedback", length=250)
	private String feedback;


	@Column(name="LicenceDocPath", length=200)
	private String licenceDocPath;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LicenceIssued", length=30)
	private Date licenceIssued;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DateOfBirth", length=30)
	private Date dob;
	
	@Column(name="Gender", length=50)
	private String gender;
	
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BadgeValidity", length=30)
	private Date badgeValidity;
	
	
	@Column(name="LicenceNumber", length=30)
	private String licenceNumber;
	
	
	@Transient
	private String appVersion;
	
	@Transient
	private String deviceModel;
	
	@Transient
	private String branchCode;
	
	
	@Transient
	private String relievingDate;
	
	@Transient
	private String ddtExpiryDate;	
	@Transient
	private String deviceId;	
	@Transient
	private String driverBatchDate;
	@Transient
	private String driverPoliceVerificationDate;
	@Transient
	private String driverMedicalExpiryDate;
	@Transient
	private String driverAntiExpiry;
	@Transient
	private String driverJoiningDate;	
	@Transient
	private String licenceValidDate;
	@Transient
	private String dobDate;
	
	@Transient
	private int userId;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LicenceValid", length=30)
	private Date licenceValid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MedicalFitnessCertificateIssued", length=30)
	private Date medicalFitnessCertificateIssued;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DDTVALID", length=30)
	private Date ddtValidDate;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DateOfJoining", length=30)
	private Date dateOfJoining;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DateOfRelease", length=30)
	private Date dateOfRelease;
	
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MedicalFitnessCertificateValid", length=30)
	private Date medicalFitnessCertificateValid;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BatchDate", length=30)
	private Date batchDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PoliceVarificationValid", length=30)
	private Date policeVerificationValid;

	@Column(name="BatchNumber", length=20)
	private String batchNumber;

	
	@Column(name="MedicalDocPath", length=100)
	private String medicalDocPath;


	@Column(name="MobileNumber", length=20)
	private String mobileNumber;

	@Column(name="PoliceVerification", length=50)
	private String policeVerification;

	@Column(name="Remarks", length=250)
	private String remarks;
	
	@Column(name="Status", length=10)
	private String status;

	@Transient
	private String vehicleNum;
	
	@Column(name="UpdatedBy", length=50)
	private String updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UpdatedTime", length=30)
	private Date updatedTime;
	
	@Transient
	private int branchId;	
	
	@Transient
	private int vendorId;	
	

	@Transient
	private String combinedFacility;


	//bi-directional many-to-one association to EFmFmVendorMaster
	@ManyToOne
	@JoinColumn(name="VENDORID")
	private EFmFmVendorMasterPO efmFmVendorMaster;

	//bi-directional many-to-one association to EFmFmVehicleCheckIn
	@OneToMany(mappedBy="efmFmDriverMaster")
	private List<EFmFmVehicleCheckInPO> efmFmVehicleCheckIns;
	
		
	public EFmFmDriverMasterPO() {
	}
	

	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public int getDriverId() {
		return this.driverId;
	}

	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}

	
	public Date getDob() {
		return dob;
	}


	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}
	


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}

	

	public String getLicenceValidDate() {
		return licenceValidDate;
	}


	public void setLicenceValidDate(String licenceValidDate) {
		this.licenceValidDate = licenceValidDate;
	}


	public Date getDdtValidDate() {
		return ddtValidDate;
	}


	public void setDdtValidDate(Date ddtValidDate) {
		this.ddtValidDate = ddtValidDate;
	}


	public Date getDateOfJoining() {
		return dateOfJoining;
	}


	public void setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}


	public Date getBatchDate() {
		return batchDate;
	}


	public void setBatchDate(Date batchDate) {
		this.batchDate = batchDate;
	}


	public Date getPoliceVerificationValid() {
		return policeVerificationValid;
	}


	public void setPoliceVerificationValid(Date policeVerificationValid) {
		this.policeVerificationValid = policeVerificationValid;
	}

	public String getDeviceId() {
		return deviceId;
	}


	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


	public String getBatchNumber() {
		return batchNumber;
	}


	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationTime() {
		return this.creationTime;
	}
	
	

	public String getDdtExpiryDate() {
		return ddtExpiryDate;
	}


	public void setDdtExpiryDate(String ddtExpiryDate) {
		this.ddtExpiryDate = ddtExpiryDate;
	}


	public String getDriverBatchDate() {
		return driverBatchDate;
	}


	public void setDriverBatchDate(String driverBatchDate) {
		this.driverBatchDate = driverBatchDate;
	}


	public String getDriverPoliceVerificationDate() {
		return driverPoliceVerificationDate;
	}


	public void setDriverPoliceVerificationDate(String driverPoliceVerificationDate) {
		this.driverPoliceVerificationDate = driverPoliceVerificationDate;
	}


	public String getDriverMedicalExpiryDate() {
		return driverMedicalExpiryDate;
	}


	public void setDriverMedicalExpiryDate(String driverMedicalExpiryDate) {
		this.driverMedicalExpiryDate = driverMedicalExpiryDate;
	}


	public String getDriverAntiExpiry() {
		return driverAntiExpiry;
	}


	public void setDriverAntiExpiry(String driverAntiExpiry) {
		this.driverAntiExpiry = driverAntiExpiry;
	}


	public String getDriverJoiningDate() {
		return driverJoiningDate;
	}


	public void setDriverJoiningDate(String driverJoiningDate) {
		this.driverJoiningDate = driverJoiningDate;
	}


	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	
	public String getVehicleNum() {
		return vehicleNum;
	}


	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}


	public String getMedicalDocPath() {
		return medicalDocPath;
	}


	public void setMedicalDocPath(String medicalDocPath) {
		this.medicalDocPath = medicalDocPath;
	}

	public String getFatherName() {
		return this.fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getFeedback() {
		return this.feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
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


	public String getLicenceDocPath() {
		return this.licenceDocPath;
	}

	public void setLicenceDocPath(String licenceDocPath) {
		this.licenceDocPath = licenceDocPath;
	}

	public Date getLicenceIssued() {
		return this.licenceIssued;
	}

	public void setLicenceIssued(Date licenceIssued) {
		this.licenceIssued = licenceIssued;
	}

	public String getLicenceNumber() {
		return this.licenceNumber;
	}

	public void setLicenceNumber(String licenceNumber) {
		this.licenceNumber = licenceNumber;
	}

	public Date getLicenceValid() {
		return this.licenceValid;
	}

	public void setLicenceValid(Date licenceValid) {
		this.licenceValid = licenceValid;
	}

	public Date getMedicalFitnessCertificateIssued() {
		return this.medicalFitnessCertificateIssued;
	}

	public void setMedicalFitnessCertificateIssued(Date medicalFitnessCertificateIssued) {
		this.medicalFitnessCertificateIssued = medicalFitnessCertificateIssued;
	}

	public Date getMedicalFitnessCertificateValid() {
		return this.medicalFitnessCertificateValid;
	}

	public void setMedicalFitnessCertificateValid(Date medicalFitnessCertificateValid) {
		this.medicalFitnessCertificateValid = medicalFitnessCertificateValid;
	}
	

	public String getMobileNumber() {
		return mobileNumber;
	}


	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}


	public String getPoliceVerification() {
		return this.policeVerification;
	}

	public void setPoliceVerification(String policeVerification) {
		this.policeVerification = policeVerification;
	}
	
	

	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getMiddleName() {
		return middleName;
	}


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public EFmFmVendorMasterPO getEfmFmVendorMaster() {
		return this.efmFmVendorMaster;
	}

	public void setEfmFmVendorMaster(EFmFmVendorMasterPO efmFmVendorMaster) {
		this.efmFmVendorMaster = efmFmVendorMaster;
	}

	public List<EFmFmVehicleCheckInPO> getEfmFmVehicleCheckIns() {
		return this.efmFmVehicleCheckIns;
	}

	public void setEfmFmVehicleCheckIns(List<EFmFmVehicleCheckInPO> efmFmVehicleCheckIns) {
		this.efmFmVehicleCheckIns = efmFmVehicleCheckIns;
	}

	public EFmFmVehicleCheckInPO addEfmFmVehicleCheckIn(EFmFmVehicleCheckInPO efmFmVehicleCheckIn) {
		getEfmFmVehicleCheckIns().add(efmFmVehicleCheckIn);
		efmFmVehicleCheckIn.setEfmFmDriverMaster(this);

		return efmFmVehicleCheckIn;
	}

	public EFmFmVehicleCheckInPO removeEfmFmVehicleCheckIn(EFmFmVehicleCheckInPO efmFmVehicleCheckIn) {
		getEfmFmVehicleCheckIns().remove(efmFmVehicleCheckIn);
		efmFmVehicleCheckIn.setEfmFmDriverMaster(null);

		return efmFmVehicleCheckIn;
	}


	public String getDobDate() {
		return dobDate;
	}


	public void setDobDate(String dobDate) {
		this.dobDate = dobDate;
	}


	public String getProfilePicPath() {
		return profilePicPath;
	}


	public void setProfilePicPath(String profilePicPath) {
		this.profilePicPath = profilePicPath;
	}
	public String getPermanentAddress() {
		return permanentAddress;
	}


	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}


	public Date getBadgeValidity() {
		return badgeValidity;
	}


	public void setBadgeValidity(Date badgeValidity) {
		this.badgeValidity = badgeValidity;
	}


	public Date getDateOfRelease() {
		return dateOfRelease;
	}


	public void setDateOfRelease(Date dateOfRelease) {
		this.dateOfRelease = dateOfRelease;
	}


	public String getRelievingDate() {
		return relievingDate;
	}


	public void setRelievingDate(String relievingDate) {
		this.relievingDate = relievingDate;
	}


	public int getBranchId() {
		return branchId;
	}


	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}


	public int getVendorId() {
		return vendorId;
	}


	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getAppVersion() {
		return appVersion;
	}


	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}


	public String getDeviceModel() {
		return deviceModel;
	}


	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}


	public String getBranchCode() {
		return branchCode;
	}


	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}


	public String getCombinedFacility() {
		return combinedFacility;
	}


	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}

	
}