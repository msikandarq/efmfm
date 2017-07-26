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
 * The persistent class for the eFmFmVehicleMaster database table.
 * 
 */
@Entity
@Table(name="eFmFmVehicleMaster")
public class EFmFmVehicleMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VehicleId", length=10)
	private int vehicleId;

	@Column(name="Capacity", length=10)
	private int capacity;

	@Column(name="InsurancePath", length=150)
	private String insurancePath;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="InsuranceValidDate", length=30)
	private Date insuranceValidDate;

	@Column(name="Status", length=10)
	private String status;
	
	@Column(name="AvailableSeat", length=10)
	private int availableSeat;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PermitValidDate", length=30)
	private Date permitValidDate;

	@Column(name="PoluctionCertificatePath", length=150)
	private String poluctionCertificatePath;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PolutionValid", length=30)
	private Date polutionValid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RegistrationDate", length=30)
	private Date registrationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="VehicleFitNessDate", length=30)
	private Date vehicleFitnessDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="VehicleMaintenanceDate", length=30)
	private Date vehicleMaintenanceDate;
		
    @Transient
	private String vehicleRegistrationDate;
       
    
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="MonthlyPendingKmUpdateDate", length=30)
	private Date monthlyPendingKmUpdateDate;
    
    @Column(name="MonthlyPendingFixedDistance")
    private double monthlyPendingFixedDistance;
    
	@Column(name="RegistartionCertificateNumber", length=150)
	private String registartionCertificateNumber;

	@Column(name="RegistartionCertificatePath", length=250)
	private String registartionCertificatePath;

	@Column(name="Remarks", length=250)
	private String remarks;

	@Column(name="TaxCertificatePath", length=150)
	private String taxCertificatePath;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TaxCertificateValid", length=30)
	private Date taxCertificateValid;

	@Column(name="UpdatedBy", length=50)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UpdatedTime", length=30)
	private Date updatedTime;

	@Column(name="VehicleEngineNumber", length=50)
	private String vehicleEngineNumber;

	@Column(name="VehicleMake", length=20)
	private String vehicleMake;

	@Column(name="VehicleModel", length=20)
	private String vehicleModel;
	
	@Column(name="ReplaceMentVehicleNum", length=20)
	private String replaceMentVehicleNum;
	
	@Column(name="VehicleGPSFitted", length=20)
	private String vehicleGPSFitted;

	@Column(name="RFIDFitted", length=20)
	private String rFIDFitted;
	
	@Column(name="VehicleACFitted", length=20)
	private String vehicleACFitted;
	
	@Column(name="VehicleModelYear", length=10)
	private String vehicleModelYear;
	
	@Column(name="TOTAL_Contact_KM")
	private int totalContactKM;

	@Column(name="PENDING_KM")
	private int pendingKM;
	
	@Column(name="Mileage")
	private double mileage;

	@Column(name="VehicleNumber", length=20)
	private String vehicleNumber;

	@Column(name="VehicleOwnerName", length=50)
	private String vehicleOwnerName;
	
	
	@Column(name="GPSLink", length=50)
	private String gPSLink;
	
	
	@Column(name="GPSUserName", length=50)
	private String gPSUserName;
	
	
	@Column(name="GPSPassword", length=50)
	private String gPSPassword;
	
	/*@Column(name="ContractDetailId", length=50)
	private int contractDetailId;*/
	
	@Transient
	private double sumTravelledDistance;
	
	@Transient
	private int contractDetailId;
	
	@Transient
	private double extraDistace;
	@Transient
	private int noOfDays;
	@Transient
	private int invoiceId;
	@Transient
	private String contractType;
	
	@Transient
	private int contractTypeCount;
	
	@Transient
	private int noOfVehicles;	
	@Transient
	private String invoiceDate;	
	
	@Transient
	private String polutionDate;
	
	@Transient
	private String distanceFlg;
	
	@Transient
	private int branchId;	
	
	@Transient
	private int tripHistoryCount;
	
	@Transient
	private int vendorId;	
	
	@Transient
	private int driverId;
	
	@Transient
	private String deviceId;
	
	@Transient
	private String vehicleUploadDoc;
	
	@Transient
	private String driverUploadDoc;
	


	@Transient
	private String taxValidDate;	
	@Transient
	private String insuranceValid;
	@Transient
	private String permitValid;	
	@Transient
	private String registrationValid;
	
	@Transient
	private String maintenanceValid;	
	
	@Transient
	private String vehicleMaintenaneDate;	
	
	@Transient
	 private String invoiceRemarks;
	
	@Transient
	private String actionType;
	@Transient
	private int invoiceNumber;
	
	@Transient
	private String fromDate;
	@Transient
	private String toDate;
	
	@Transient
	private double fuelExtraAmount;
	
	@Transient
	private String firstName;
	
	@Transient
	private String mobileNumber;
	
	@Transient
	private String licenceNumber;
	
	@Transient
	private String licenceValidDate;
	
	@Transient
	private String policeVerificationValid;
	
	@Transient
	private int userId;	
	
	@Transient
	private String userRole;	
	
	
	@Transient
	private String statusFlg;
	
	@Transient
	private String addVehicle;
	
	@Transient
	private String addDriver;
	
	@Transient
	private String invoiceAcptType;
	
	@Transient
	private String combinedFacility;
	
	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}

	//bi-directional many-to-one association to EFmFmVehicleCheckIn
	@OneToMany(mappedBy="efmFmVehicleMaster")
	private List<EFmFmVehicleCheckInPO> efmFmVehicleCheckIns;
	
	//bi-directional many-to-one association to EFmFmVehicleCheckIn
	@OneToMany(mappedBy="efmFmVehicleMaster")
	private List<EFmFmVehicleInspectionPO> eFmFmVehicleInspection;
	
	//bidirectional manytoone association to eFmFmVendorContractInvoice
	 @OneToMany(mappedBy="efmFmVehicleMaster")
	 private List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoice;



	//bi-directional many-to-one association to EFmFmVendorMaster
	@ManyToOne
	@JoinColumn(name="DistanceContractId")
	private EFmFmFixedDistanceContractDetailPO eFmFmContractDetails;
	
		
	@ManyToOne
	@JoinColumn(name="VendorId")
	private EFmFmVendorMasterPO efmFmVendorMaster;
	
/*	@ManyToOne
	@JoinColumn(name="ContractTypeId")
	private EFmFmVendorContractTypeMasterPO eFmFmVendorContractTypeMaster;*/
	
			


	public EFmFmVehicleMasterPO() {
	}

	public int getVehicleId() {
		return this.vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getInsurancePath() {
		return this.insurancePath;
	}

	public void setInsurancePath(String insurancePath) {
		this.insurancePath = insurancePath;
	}

	public Date getInsuranceValidDate() {
		return this.insuranceValidDate;
	}

	public void setInsuranceValidDate(Date insuranceValidDate) {
		this.insuranceValidDate = insuranceValidDate;
	}

	
	
	public Date getVehicleFitnessDate() {
		return vehicleFitnessDate;
	}

	public void setVehicleFitnessDate(Date vehicleFitnessDate) {
		this.vehicleFitnessDate = vehicleFitnessDate;
	}

	/*public int getAvailableSeat() {
		return availableSeat;
	}

	public void setAvailableSeat(int availableSeat) {
		this.availableSeat = availableSeat;
	}*/

	public int getTotalContactKM() {
		return totalContactKM;
	}

	public void setTotalContactKM(int totalContactKM) {
		this.totalContactKM = totalContactKM;
	}


	public int getPendingKM() {
		return pendingKM;
	}

	public void setPendingKM(int pendingKM) {
		this.pendingKM = pendingKM;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getVehicleRegistrationDate() {
		return vehicleRegistrationDate;
	}

	public void setVehicleRegistrationDate(String vehicleRegistrationDate) {
		this.vehicleRegistrationDate = vehicleRegistrationDate;
	}

	public Date getPermitValidDate() {
		return this.permitValidDate;
	}

	public void setPermitValidDate(Date permitValidDate) {
		this.permitValidDate = permitValidDate;
	}

	public String getPoluctionCertificatePath() {
		return this.poluctionCertificatePath;
	}

	public void setPoluctionCertificatePath(String poluctionCertificatePath) {
		this.poluctionCertificatePath = poluctionCertificatePath;
	}

	public Date getPolutionValid() {
		return this.polutionValid;
	}

	public void setPolutionValid(Date polutionValid) {
		this.polutionValid = polutionValid;
	}

	public String getRegistartionCertificateNumber() {
		return this.registartionCertificateNumber;
	}

	public void setRegistartionCertificateNumber(String registartionCertificateNumber) {
		this.registartionCertificateNumber = registartionCertificateNumber;
	}

	public String getRegistartionCertificatePath() {
		return this.registartionCertificatePath;
	}

	
	
	public String getVehicleGPSFitted() {
		return vehicleGPSFitted;
	}

	public void setVehicleGPSFitted(String vehicleGPSFitted) {
		this.vehicleGPSFitted = vehicleGPSFitted;
	}

	public String getrFIDFitted() {
		return rFIDFitted;
	}

	public void setrFIDFitted(String rFIDFitted) {
		this.rFIDFitted = rFIDFitted;
	}

	public String getVehicleACFitted() {
		return vehicleACFitted;
	}

	public void setVehicleACFitted(String vehicleACFitted) {
		this.vehicleACFitted = vehicleACFitted;
	}

	public void setRegistartionCertificatePath(String registartionCertificatePath) {
		this.registartionCertificatePath = registartionCertificatePath;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTaxCertificatePath() {
		return this.taxCertificatePath;
	}

	public void setTaxCertificatePath(String taxCertificatePath) {
		this.taxCertificatePath = taxCertificatePath;
	}

	public Date getTaxCertificateValid() {
		return this.taxCertificateValid;
	}

	public void setTaxCertificateValid(Date taxCertificateValid) {
		this.taxCertificateValid = taxCertificateValid;
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

	public String getVehicleEngineNumber() {
		return this.vehicleEngineNumber;
	}

	public void setVehicleEngineNumber(String vehicleEngineNumber) {
		this.vehicleEngineNumber = vehicleEngineNumber;
	}

	public String getVehicleMake() {
		return this.vehicleMake;
	}

	public void setVehicleMake(String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}

	public String getVehicleModel() {
		return this.vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public String getVehicleModelYear() {
		return this.vehicleModelYear;
	}

	public void setVehicleModelYear(String vehicleModelYear) {
		this.vehicleModelYear = vehicleModelYear;
	}

	

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleOwnerName() {
		return this.vehicleOwnerName;
	}

	public void setVehicleOwnerName(String vehicleOwnerName) {
		this.vehicleOwnerName = vehicleOwnerName;
	}

	public List<EFmFmVehicleCheckInPO> getEfmFmVehicleCheckIns() {
		return this.efmFmVehicleCheckIns;
	}

	public void setEfmFmVehicleCheckIns(List<EFmFmVehicleCheckInPO> efmFmVehicleCheckIns) {
		this.efmFmVehicleCheckIns = efmFmVehicleCheckIns;
	}

	public EFmFmVehicleCheckInPO addEfmFmVehicleCheckIn(EFmFmVehicleCheckInPO efmFmVehicleCheckIn) {
		getEfmFmVehicleCheckIns().add(efmFmVehicleCheckIn);
		efmFmVehicleCheckIn.setEfmFmVehicleMaster(this);

		return efmFmVehicleCheckIn;
	}

	public EFmFmVehicleCheckInPO removeEfmFmVehicleCheckIn(EFmFmVehicleCheckInPO efmFmVehicleCheckIn) {
		getEfmFmVehicleCheckIns().remove(efmFmVehicleCheckIn);
		efmFmVehicleCheckIn.setEfmFmVehicleMaster(null);

		return efmFmVehicleCheckIn;
	}

	public EFmFmVendorMasterPO getEfmFmVendorMaster() {
		return this.efmFmVendorMaster;
	}

	public void setEfmFmVendorMaster(EFmFmVendorMasterPO efmFmVendorMaster) {
		this.efmFmVendorMaster = efmFmVendorMaster;
	}

/*
	public int getContractDetailId() {
		return contractDetailId;
	}

	public void setContractDetailId(int contractDetailId) {
		this.contractDetailId = contractDetailId;
	}
*/
	/*public EFmFmVendorContractTypeMasterPO geteFmFmVendorContractTypeMaster() {
		return eFmFmVendorContractTypeMaster;
	}

	public void seteFmFmVendorContractTypeMaster(
			EFmFmVendorContractTypeMasterPO eFmFmVendorContractTypeMaster) {
		this.eFmFmVendorContractTypeMaster = eFmFmVendorContractTypeMaster;
	}*/

	public double getSumTravelledDistance() {
		return sumTravelledDistance;
	}

	public void setSumTravelledDistance(double sumTravelledDistance) {
		this.sumTravelledDistance = sumTravelledDistance;
	}

	public int getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public int getNoOfVehicles() {
		return noOfVehicles;
	}

	public void setNoOfVehicles(int noOfVehicles) {
		this.noOfVehicles = noOfVehicles;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public int getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(int invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getPolutionDate() {
		return polutionDate;
	}

	public void setPolutionDate(String polutionDate) {
		this.polutionDate = polutionDate;
	}

	public String getTaxValidDate() {
		return taxValidDate;
	}

	public void setTaxValidDate(String taxValidDate) {
		this.taxValidDate = taxValidDate;
	}

	public String getInsuranceValid() {
		return insuranceValid;
	}

	public void setInsuranceValid(String insuranceValid) {
		this.insuranceValid = insuranceValid;
	}

	public String getPermitValid() {
		return permitValid;
	}

	public void setPermitValid(String permitValid) {
		this.permitValid = permitValid;
	}

	public String getRegistrationValid() {
		return registrationValid;
	}

	public void setRegistrationValid(String registrationValid) {
		this.registrationValid = registrationValid;
	}

	public String getMaintenanceValid() {
		return maintenanceValid;
	}

	public void setMaintenanceValid(String maintenanceValid) {
		this.maintenanceValid = maintenanceValid;
	}

	public List<EFmFmVehicleInspectionPO> geteFmFmVehicleInspection() {
		return eFmFmVehicleInspection;
	}

	public void seteFmFmVehicleInspection(
			List<EFmFmVehicleInspectionPO> eFmFmVehicleInspection) {
		this.eFmFmVehicleInspection = eFmFmVehicleInspection;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public List<EFmFmVendorContractInvoicePO> geteFmFmVendorContractInvoice() {
		return eFmFmVendorContractInvoice;
	}

	public void seteFmFmVendorContractInvoice(List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoice) {
		this.eFmFmVendorContractInvoice = eFmFmVendorContractInvoice;
	}

	public String getReplaceMentVehicleNum() {
		return replaceMentVehicleNum;
	}

	public void setReplaceMentVehicleNum(String replaceMentVehicleNum) {
		this.replaceMentVehicleNum = replaceMentVehicleNum;
	}

	public double getExtraDistace() {
		return extraDistace;
	}

	public void setExtraDistace(double extraDistace) {
		this.extraDistace = extraDistace;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

    public String getInvoiceRemarks() {
        return invoiceRemarks;
    }

    public void setInvoiceRemarks(String invoiceRemarks) {
        this.invoiceRemarks = invoiceRemarks;
    }

	public double getMileage() {
		return mileage;
	}

	public void setMileage(double mileage) {
		this.mileage = mileage;
	}

	public String getStatusFlg() {
		return statusFlg;
	}

	public void setStatusFlg(String statusFlg) {
		this.statusFlg = statusFlg;
	}

	public double getFuelExtraAmount() {
		return fuelExtraAmount;
	}

	public void setFuelExtraAmount(double fuelExtraAmount) {
		this.fuelExtraAmount = fuelExtraAmount;
	}

	public double getMonthlyPendingFixedDistance() {
		return monthlyPendingFixedDistance;
	}

	public void setMonthlyPendingFixedDistance(double monthlyPendingFixedDistance) {
		this.monthlyPendingFixedDistance = monthlyPendingFixedDistance;
	}

	public Date getMonthlyPendingKmUpdateDate() {
		return monthlyPendingKmUpdateDate;
	}

	public void setMonthlyPendingKmUpdateDate(Date monthlyPendingKmUpdateDate) {
		this.monthlyPendingKmUpdateDate = monthlyPendingKmUpdateDate;
	}

	public int getVendorId() {
		return vendorId;
	}

	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getLicenceNumber() {
		return licenceNumber;
	}

	public void setLicenceNumber(String licenceNumber) {
		this.licenceNumber = licenceNumber;
	}

	public String getLicenceValidDate() {
		return licenceValidDate;
	}

	public void setLicenceValidDate(String licenceValidDate) {
		this.licenceValidDate = licenceValidDate;
	}

	public String getPoliceVerificationValid() {
		return policeVerificationValid;
	}

	public void setPoliceVerificationValid(String policeVerificationValid) {
		this.policeVerificationValid = policeVerificationValid;
	}

	public int getDriverId() {
		return driverId;
	}

	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDistanceFlg() {
		return distanceFlg;
	}

	public void setDistanceFlg(String distanceFlg) {
		this.distanceFlg = distanceFlg;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	

	public int getContractDetailId() {
		return contractDetailId;
	}

	public void setContractDetailId(int contractDetailId) {
		this.contractDetailId = contractDetailId;
	}

	public EFmFmFixedDistanceContractDetailPO geteFmFmContractDetails() {
		return eFmFmContractDetails;
	}

	public void seteFmFmContractDetails(EFmFmFixedDistanceContractDetailPO eFmFmContractDetails) {
		this.eFmFmContractDetails = eFmFmContractDetails;
	}

	public String getAddVehicle() {
		return addVehicle;
	}

	public void setAddVehicle(String addVehicle) {
		this.addVehicle = addVehicle;
	}

	public String getAddDriver() {
		return addDriver;
	}

	public void setAddDriver(String addDriver) {
		this.addDriver = addDriver;
	}

	public String getInvoiceAcptType() {
		return invoiceAcptType;
	}

	public void setInvoiceAcptType(String invoiceAcptType) {
		this.invoiceAcptType = invoiceAcptType;
	}

	public String getVehicleUploadDoc() {
		return vehicleUploadDoc;
	}

	public void setVehicleUploadDoc(String vehicleUploadDoc) {
		this.vehicleUploadDoc = vehicleUploadDoc;
	}

	public String getDriverUploadDoc() {
		return driverUploadDoc;
	}

	public void setDriverUploadDoc(String driverUploadDoc) {
		this.driverUploadDoc = driverUploadDoc;
	}

	public int getAvailableSeat() {
		return availableSeat;
	}

	public void setAvailableSeat(int availableSeat) {
		this.availableSeat = availableSeat;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public int getContractTypeCount() {
		return contractTypeCount;
	}

	public void setContractTypeCount(int contractTypeCount) {
		this.contractTypeCount = contractTypeCount;
	}

	public Date getVehicleMaintenanceDate() {
		return vehicleMaintenanceDate;
	}

	public void setVehicleMaintenanceDate(Date vehicleMaintenanceDate) {
		this.vehicleMaintenanceDate = vehicleMaintenanceDate;
	}

	public String getVehicleMaintenaneDate() {
		return vehicleMaintenaneDate;
	}

	public void setVehicleMaintenaneDate(String vehicleMaintenaneDate) {
		this.vehicleMaintenaneDate = vehicleMaintenaneDate;
	}

	public int getTripHistoryCount() {
		return tripHistoryCount;
	}

	public void setTripHistoryCount(int tripHistoryCount) {
		this.tripHistoryCount = tripHistoryCount;
	}

	public String getgPSLink() {
		return gPSLink;
	}

	public void setgPSLink(String gPSLink) {
		this.gPSLink = gPSLink;
	}

	public String getgPSUserName() {
		return gPSUserName;
	}

	public void setgPSUserName(String gPSUserName) {
		this.gPSUserName = gPSUserName;
	}

	public String getgPSPassword() {
		return gPSPassword;
	}

	public void setgPSPassword(String gPSPassword) {
		this.gPSPassword = gPSPassword;
	}

	
	
}