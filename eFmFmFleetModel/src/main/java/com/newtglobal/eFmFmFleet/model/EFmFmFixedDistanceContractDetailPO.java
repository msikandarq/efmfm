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
 * The persistent class for the eFmFmFixedDistanceContractDetail database table.
 * 
 */
@Entity
@Table(name="eFmFmContractDetails")
public class EFmFmFixedDistanceContractDetailPO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="DistanceContractId", length=15)
    int distanceContractId;
    
    @Column(name="FixedDistanceMonthly", length=10)
    private double fixedDistanceMonthly;
     
    @Column(name="FixedDistancePrDay", length=10)
    private double fixedDistancePrDay;
    
    
    @Column(name="MinimumDays", length=10)
    private int minimumDays;


    
    @Column(name="ExtraDistanceChargeRate", length=50)
    private double extraDistanceChargeRate;
        
    
    @Column(name="FixedDistanceChargeRate", length=50)
    private double fixedDistanceChargeRate;
    
    @Column(name="PenaltyInPercentagePerDay", length=50)
    private double penaltyInPercentagePerDay;
    
    @Column(name="Penalty", length=10)
    private String penalty;

    @Column(name="PerDayCost", length=50)
    private double perDayCost;
    
    @Column(name="PerKmCost", length=50)
    private double perKmCost;
    

    @Column(name="PetrolPrice", length=50)
    private double petrolPrice;
        
    
    @Column(name="Created_By", length=50)
    private String createdBy;
    
    @Column(name="Status", length=50)
    private String status;
    
    @Column(name="ContractTittle", length=100)
    private String contractTitle;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CreationTime", length=30)
    private Date creationTime;


    @Column(name="UpdatedBy", length=50)
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="UpdatedTime", length=30)
    private Date updatedTime;
    
    @Temporal( TemporalType.TIMESTAMP ) 
    @Column(name="FromDate", length=30)
    private Date fromDate;
    
    @Temporal( TemporalType.TIMESTAMP ) 
    @Column(name="ToDate", length=30)
    private Date toDate;
    
    @Column(name="CloneId", length=15)
    private int cloneId;
    
    @Column(name="FuelPriceCalculation", length=10)
    private String fuelPriceCalculation;
    
    @Transient
    int branchId;
    
    @Transient
    private String startDate;
    
    @Transient
    private String endDate;
    
	@Transient
	private int userId;	

    //bi-directional many-to-one association to EFmFmClientBranchPO
    @ManyToOne
    @JoinColumn(name="contractTypeId")
    private EFmFmVendorContractTypeMasterPO eFmFmVendorContractTypeMaster;
    
	@OneToMany(mappedBy = "eFmFmContractDetails")
	private List<EFmFmVehicleMasterPO> efmFmVehicleMasters;
	
	@OneToMany(mappedBy="eFmFmContractDetails")
	private List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoice;
	
    @ManyToOne
    @JoinColumn(name="FuelTypeId")
    private EFmFmVendorFuelContractTypeMasterPO eFmFmVendorFuelContractTypeMaster;
    
    
    @ManyToOne
    @JoinColumn(name="BranchId")
    private EFmFmClientBranchPO eFmFmClientBranchPO;

    public EFmFmFixedDistanceContractDetailPO() {
    }



    public int getDistanceContractId() {
        return distanceContractId;
    }



    public void setDistanceContractId(int distanceContractId) {
        this.distanceContractId = distanceContractId;
    }

    public double getFixedDistanceMonthly() {
        return fixedDistanceMonthly;
    }



    public void setFixedDistanceMonthly(double fixedDistanceMonthly) {
        this.fixedDistanceMonthly = fixedDistanceMonthly;
    }



    public double getFixedDistancePrDay() {
        return fixedDistancePrDay;
    }



    public void setFixedDistancePrDay(double fixedDistancePrDay) {
        this.fixedDistancePrDay = fixedDistancePrDay;
    }



    public int getMinimumDays() {
        return minimumDays;
    }



    public void setMinimumDays(int minimumDays) {
        this.minimumDays = minimumDays;
    }



    public double getExtraDistanceChargeRate() {
        return extraDistanceChargeRate;
    }



    public void setExtraDistanceChargeRate(double extraDistanceChargeRate) {
        this.extraDistanceChargeRate = extraDistanceChargeRate;
    }



    public double getFixedDistanceChargeRate() {
        return fixedDistanceChargeRate;
    }



    public void setFixedDistanceChargeRate(double fixedDistanceChargeRate) {
        this.fixedDistanceChargeRate = fixedDistanceChargeRate;
    }



    public String getPenalty() {
        return penalty;
    }



    public void setPenalty(String penalty) {
        this.penalty = penalty;
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

    public double getPenaltyInPercentagePerDay() {
        return penaltyInPercentagePerDay;
    }


    public void setPenaltyInPercentagePerDay(double penaltyInPercentagePerDay) {
        this.penaltyInPercentagePerDay = penaltyInPercentagePerDay;
    }



    public EFmFmVendorContractTypeMasterPO geteFmFmVendorContractTypeMaster() {
        return eFmFmVendorContractTypeMaster;
    }



    public void seteFmFmVendorContractTypeMaster(
            EFmFmVendorContractTypeMasterPO eFmFmVendorContractTypeMaster) {
        this.eFmFmVendorContractTypeMaster = eFmFmVendorContractTypeMaster;
    }

    public int getBranchId() {
        return branchId;
    }



    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }



    public String getStatus() {
        return status;
    }



    public void setStatus(String status) {
        this.status = status;
    }



    public String getContractTitle() {
        return contractTitle;
    }



    public void setContractTitle(String contractTitle) {
        this.contractTitle = contractTitle;
    }



    



    public String getStartDate() {
        return startDate;
    }



    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }



    public String getEndDate() {
        return endDate;
    }



    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }



    public Date getFromDate() {
        return fromDate;
    }



    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }



    public Date getToDate() {
        return toDate;
    }



    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }



    public EFmFmClientBranchPO geteFmFmClientBranchPO() {
        return eFmFmClientBranchPO;
    }



    public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
        this.eFmFmClientBranchPO = eFmFmClientBranchPO;
    }



	public int getCloneId() {
		return cloneId;
	}



	public void setCloneId(int cloneId) {
		this.cloneId = cloneId;
	}



	public double getPerDayCost() {
		return perDayCost;
	}



	public void setPerDayCost(double perDayCost) {
		this.perDayCost = perDayCost;
	}



	public double getPetrolPrice() {
		return petrolPrice;
	}



	public void setPetrolPrice(double petrolPrice) {
		this.petrolPrice = petrolPrice;
	}



	public EFmFmVendorFuelContractTypeMasterPO geteFmFmVendorFuelContractTypeMaster() {
		return eFmFmVendorFuelContractTypeMaster;
	}



	public void seteFmFmVendorFuelContractTypeMaster(
			EFmFmVendorFuelContractTypeMasterPO eFmFmVendorFuelContractTypeMaster) {
		this.eFmFmVendorFuelContractTypeMaster = eFmFmVendorFuelContractTypeMaster;
	}



	public int getUserId() {
		return userId;
	}



	public void setUserId(int userId) {
		this.userId = userId;
	}



	public String getFuelPriceCalculation() {
		return fuelPriceCalculation;
	}



	public void setFuelPriceCalculation(String fuelPriceCalculation) {
		this.fuelPriceCalculation = fuelPriceCalculation;
	}






	public List<EFmFmVendorContractInvoicePO> geteFmFmVendorContractInvoice() {
		return eFmFmVendorContractInvoice;
	}



	public void seteFmFmVendorContractInvoice(List<EFmFmVendorContractInvoicePO> eFmFmVendorContractInvoice) {
		this.eFmFmVendorContractInvoice = eFmFmVendorContractInvoice;
	}



	public List<EFmFmVehicleMasterPO> getEfmFmVehicleMasters() {
		return efmFmVehicleMasters;
	}



	public void setEfmFmVehicleMasters(List<EFmFmVehicleMasterPO> efmFmVehicleMasters) {
		this.efmFmVehicleMasters = efmFmVehicleMasters;
	}

	public double getPerKmCost() {
		return perKmCost;
	}



	public void setPerKmCost(double perKmCost) {
		this.perKmCost = perKmCost;
	}



	
    
}