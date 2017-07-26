package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * The persistent class for the eFmFmVendorContractInvoice database table.
 * 
 */
@Entity
@Table(name="eFmFmVendorContractInvoice")
public class EFmFmVendorContractInvoicePO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ContractInvoiceId", length=15)
	private int invoiceId;
	
	@Column(name="InvoiceStatus", length=10)
	private String invoiceStatus;
	
	@Column(name="InvoiceType", length=200)
	private String invoiceType;

	@Column(name="InvoiceNumber", length=10)
	private long invoiceNumber;
	
	@Column(name="TotalExtraDistance", length=50)
	private double totalExtraDistance;
	
	@Column(name="TotalExtraHour", length=50)
	private double totalExtraHour;
	
	@Column(name="TravelledDistance", length=50)
	private double travelledDistance;
		
	@Column(name="TotalAbsenceHours", length=50)
	private double totalAbsenceHours;
	
	@Column(name="TotalPenalityAmount", length=50)
	private double totalPenalityAmount;

	@Column(name="TotalPerDayDeductionAmnt", length=50)
	private double totalPerDayDeductionAmnt;
	
	@Column(name="TotalAmountPayable", length=50)
	private double totalAmountPayable;
	
	@Column(name="BaseTotal", length=50)
	private double baseTotal;
	
	@Column(name="TotalDeductibles", length=50)
	private double totalDeductibles;
	
	@Column(name="BaseDistance", length=50)
	private double baseDistance;	
	
	@Column(name="TripTotalAmount", length=50)
	private double tripTotalAmount;
	
	@Column(name="ExtraDistanceCharge", length=50)
	private double extraDistanceCharge;
	
	@Column(name="TotalDistance", length=50)
	private double totalDistance;
	
	@Column(name="WorkingDays", length=50)
	private int workingDays;
	
	@Column(name="DistanceFlg", length=20)
	private String distanceFlg;
	
	@Column(name="ModifiedTravelledDistance",length=50)
	private double modTravelledDistance;
	
	@Column(name="ModifiedTotalExtraDistance",length=50)
	private double modTotalExtraDistance;
	
	@Column(name="ModifiedTotPenalityAmount",length=50)
	private double modTotPenalityAmount;

	@Column(name="ModifiedTotPerDayDeductionAmnt",length=50)
	private double modTotPerDayDeductionAmnt;

	@Column(name="ModifiedTotalAmountPayable",length=50)
	private double modTotalAmountPayable;
	
	@Column(name="ModifiedTotalDeductibles",length=50)
	private double modTotalDeductibles;
	
	@Column(name="ModifiedTripTotalAmount",length=50)
	private double modTripTotalAmount;
	
	@Column(name="ModifiedExtraDistanceCharge",length=50)
	private double modExtraDistanceCharge;
	
	@Column(name="ModifiedTotalDistance",length=50)
	private double modTotalDistance;	
	
	@Column(name="ModifiedPresentDays",length=50)
	private int modPresentDays;
	
	/*@Column(name="ContractDetailsId",length=50)
	private int contractDetailsId;*/

	@Column(name="FuelExtraAmount",length=50)
	private double fuelExtraAmount;
	
	@Column(name="ModifiedFuelExtraAmount",length=50)
	private double modifiedFuelExtraAmount;
	
	@Column(name="PerDayCost",length=50)
	private double perDayCost;	
	
	@Column(name="TotalKmAmount",length=50)
	private double totalKmAmount;	
	
	
	@Column(name="Mileage",length=50)
	private double mileage;
	
	/*public int getContractDetailsId() {
		return contractDetailsId;
	}
*/

	public double getFuelExtraAmount() {
		return fuelExtraAmount;
	}


	public double getModifiedFuelExtraAmount() {
		return modifiedFuelExtraAmount;
	}


/*	public void setContractDetailsId(int contractDetailsId) {
		this.contractDetailsId = contractDetailsId;
	}*/


	public void setFuelExtraAmount(double fuelExtraAmount) {
		this.fuelExtraAmount = fuelExtraAmount;
	}


	public void setModifiedFuelExtraAmount(double modifiedFuelExtraAmount) {
		this.modifiedFuelExtraAmount = modifiedFuelExtraAmount;
	}


	@Column(name="InvoiceRemarks", length=200,columnDefinition="TEXT(200)")
    private String invoiceRemarks;
	
	@Column(name="ModifiedInvoiceRemarks", length=200,columnDefinition="TEXT(200)")
    private String modifiedInvoiceRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ModifiedInvoiceGenerationDate", length=30)
	private Date modInvoiceGenerationDate;
	
	@Column(name="PresentDays", length=50)
	private int presentDays;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="InvoiceEndDate", length=30)
	private Date invoiceEndDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="InvoiceStartDate", length=30)
	private Date invoiceStartDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="InvoiceGenerationDate", length=30)
	private Date invoiceGenerationDate;
	
	@Column(name="Created_By", length=50)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreationTime", length=30)
	private Date creationTime;


	@Column(name="UpdatedBy", length=50)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UpdatedTime", length=30)
	private Date updatedTime;
	
	@Column(name="ApprovalStatus", length=200)
	private String approvalStatus;

	
	@Transient
	private int userId;
	
	//bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;
	
	@ManyToOne
	@JoinColumn(name="DistanceContractId")
	private EFmFmFixedDistanceContractDetailPO eFmFmContractDetails;
		
		
	//bi-directional many-to-one association to EFmFmVendorMaster
	@ManyToOne
	@JoinColumn(name="VehicleId")
	private EFmFmVehicleMasterPO efmFmVehicleMaster;
	
	@ManyToOne
	@JoinColumn(name="AssignRouteId")
	private EFmFmAssignRoutePO efmFmAssignRoute;

	public EFmFmVendorContractInvoicePO() {
	}

	
	public double getBaseDistance() {
		return baseDistance;
	}


	public double getTravelledDistance() {
		return travelledDistance;
	}


	public void setTravelledDistance(double travelledDistance) {
		this.travelledDistance = travelledDistance;
	}


	public void setBaseDistance(double baseDistance) {
		this.baseDistance = baseDistance;
	}


	public double getTripTotalAmount() {
		return tripTotalAmount;
	}


	public void setTripTotalAmount(double tripTotalAmount) {
		this.tripTotalAmount = tripTotalAmount;
	}


	public double getTotalDistance() {
		return totalDistance;
	}


	public void setTotalDistance(double totalDistance) {
		this.totalDistance = totalDistance;
	}


	public int getWorkingDays() {
		return workingDays;
	}


	public void setWorkingDays(int workingDays) {
		this.workingDays = workingDays;
	}


	public int getPresentDays() {
		return presentDays;
	}


	public void setPresentDays(int presentDays) {
		this.presentDays = presentDays;
	}


	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public long getInvoiceNumber() {
		return invoiceNumber;
	}


	public void setInvoiceNumber(long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}


	public double getTotalExtraDistance() {
		return totalExtraDistance;
	}

	public void setTotalExtraDistance(double totalExtraDistance) {
		this.totalExtraDistance = totalExtraDistance;
	}

	public double getTotalExtraHour() {
		return totalExtraHour;
	}

	public void setTotalExtraHour(double totalExtraHour) {
		this.totalExtraHour = totalExtraHour;
	}

	public double getTotalAbsenceHours() {
		return totalAbsenceHours;
	}

	public void setTotalAbsenceHours(double totalAbsenceHours) {
		this.totalAbsenceHours = totalAbsenceHours;
	}

	public double getTotalAmountPayable() {
		return totalAmountPayable;
	}

	public void setTotalAmountPayable(double totalAmountPayable) {
		this.totalAmountPayable = totalAmountPayable;
	}

	public double getBaseTotal() {
		return baseTotal;
	}

	public void setBaseTotal(double baseTotal) {
		this.baseTotal = baseTotal;
	}

	public double getTotalDeductibles() {
		return totalDeductibles;
	}

	public void setTotalDeductibles(double totalDeductibles) {
		this.totalDeductibles = totalDeductibles;
	}

	public Date getInvoiceEndDate() {
		return invoiceEndDate;
	}

	public void setInvoiceEndDate(Date invoiceEndDate) {
		this.invoiceEndDate = invoiceEndDate;
	}

	

	public Date getInvoiceGenerationDate() {
		return invoiceGenerationDate;
	}

	public void setInvoiceGenerationDate(Date invoiceGenerationDate) {
		this.invoiceGenerationDate = invoiceGenerationDate;
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}


	public EFmFmAssignRoutePO getEfmFmAssignRoute() {
		return efmFmAssignRoute;
	}


	public void setEfmFmAssignRoute(EFmFmAssignRoutePO efmFmAssignRoute) {
		this.efmFmAssignRoute = efmFmAssignRoute;
	}


	public double getExtraDistanceCharge() {
		return extraDistanceCharge;
	}


	public void setExtraDistanceCharge(double extraDistanceCharge) {
		this.extraDistanceCharge = extraDistanceCharge;
	}


	public double getTotalPenalityAmount() {
		return totalPenalityAmount;
	}


	public void setTotalPenalityAmount(double totalPenalityAmount) {
		this.totalPenalityAmount = totalPenalityAmount;
	}


	public double getTotalPerDayDeductionAmnt() {
		return totalPerDayDeductionAmnt;
	}


	public void setTotalPerDayDeductionAmnt(double totalPerDayDeductionAmnt) {
		this.totalPerDayDeductionAmnt = totalPerDayDeductionAmnt;
	}


	public EFmFmVehicleMasterPO getEfmFmVehicleMaster() {
		return efmFmVehicleMaster;
	}


	public void setEfmFmVehicleMaster(EFmFmVehicleMasterPO efmFmVehicleMaster) {
		this.efmFmVehicleMaster = efmFmVehicleMaster;
	}


	public double getModTravelledDistance() {
		return modTravelledDistance;
	}


	public void setModTravelledDistance(double modTravelledDistance) {
		this.modTravelledDistance = modTravelledDistance;
	}


	public double getModTotalExtraDistance() {
		return modTotalExtraDistance;
	}


	public void setModTotalExtraDistance(double modTotalExtraDistance) {
		this.modTotalExtraDistance = modTotalExtraDistance;
	}


	public double getModTotPenalityAmount() {
		return modTotPenalityAmount;
	}


	public void setModTotPenalityAmount(double modTotPenalityAmount) {
		this.modTotPenalityAmount = modTotPenalityAmount;
	}


	public double getModTotPerDayDeductionAmnt() {
		return modTotPerDayDeductionAmnt;
	}


	public void setModTotPerDayDeductionAmnt(double modTotPerDayDeductionAmnt) {
		this.modTotPerDayDeductionAmnt = modTotPerDayDeductionAmnt;
	}


	public double getModTotalAmountPayable() {
		return modTotalAmountPayable;
	}


	public void setModTotalAmountPayable(double modTotalAmountPayable) {
		this.modTotalAmountPayable = modTotalAmountPayable;
	}


	public double getModTotalDeductibles() {
		return modTotalDeductibles;
	}


	public void setModTotalDeductibles(double modTotalDeductibles) {
		this.modTotalDeductibles = modTotalDeductibles;
	}


	public double getModTripTotalAmount() {
		return modTripTotalAmount;
	}


	public void setModTripTotalAmount(double modTripTotalAmount) {
		this.modTripTotalAmount = modTripTotalAmount;
	}


	public double getModExtraDistanceCharge() {
		return modExtraDistanceCharge;
	}


	public void setModExtraDistanceCharge(double modExtraDistanceCharge) {
		this.modExtraDistanceCharge = modExtraDistanceCharge;
	}


	public double getModTotalDistance() {
		return modTotalDistance;
	}


	public void setModTotalDistance(double modTotalDistance) {
		this.modTotalDistance = modTotalDistance;
	}


	public int getModPresentDays() {
		return modPresentDays;
	}


	public void setModPresentDays(int modPresentDays) {
		this.modPresentDays = modPresentDays;
	}


	public Date getModInvoiceGenerationDate() {
		return modInvoiceGenerationDate;
	}


	public void setModInvoiceGenerationDate(Date modInvoiceGenerationDate) {
		this.modInvoiceGenerationDate = modInvoiceGenerationDate;
	}


    public String getInvoiceRemarks() {
        return invoiceRemarks;
    }


    public void setInvoiceRemarks(String invoiceRemarks) {
        this.invoiceRemarks = invoiceRemarks;
    }


	public String getApprovalStatus() {
		return approvalStatus;
	}


	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}


	public double getMileage() {
		return mileage;
	}


	public void setMileage(double mileage) {
		this.mileage = mileage;
	}


	public String getDistanceFlg() {
		return distanceFlg;
	}


	public void setDistanceFlg(String distanceFlg) {
		this.distanceFlg = distanceFlg;
	}


	public Date getInvoiceStartDate() {
		return invoiceStartDate;
	}


	public void setInvoiceStartDate(Date invoiceStartDate) {
		this.invoiceStartDate = invoiceStartDate;
	}


	public double getPerDayCost() {
		return perDayCost;
	}


	public void setPerDayCost(double perDayCost) {
		this.perDayCost = perDayCost;
	}


	public double getTotalKmAmount() {
		return totalKmAmount;
	}


	public void setTotalKmAmount(double totalKmAmount) {
		this.totalKmAmount = totalKmAmount;
	}


	public EFmFmFixedDistanceContractDetailPO geteFmFmContractDetails() {
		return eFmFmContractDetails;
	}


	public void seteFmFmContractDetails(EFmFmFixedDistanceContractDetailPO eFmFmContractDetails) {
		this.eFmFmContractDetails = eFmFmContractDetails;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getModifiedInvoiceRemarks() {
		return modifiedInvoiceRemarks;
	}


	public void setModifiedInvoiceRemarks(String modifiedInvoiceRemarks) {
		this.modifiedInvoiceRemarks = modifiedInvoiceRemarks;
	}


	
	
	
	
}