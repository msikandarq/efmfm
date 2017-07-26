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
 * The persistent class for the eFmFmFixedDistanceContractDetail database table.
 * 
 */

@Entity
@Table(name="eFmFmFuelCharge")
public class EFmFmFuelChargesPO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="FuelChargeId", length=15)
    int fuelChargeId;
    
    @Temporal( TemporalType.TIMESTAMP ) 
    @Column(name="FromDate", length=30)
    private Date fromDate;
    
    @Temporal( TemporalType.TIMESTAMP ) 
    @Column(name="ToDate", length=30)
    private Date toDate;
    
    @Column(name="NewPrice", length=10)
    private double newPrice;
    
 
    
    @Column(name="ContractTitle", length=200)
    private String contractTitle;
    
    @Column(name="TotalAmount", length=10)
    private double totalAmount;
    
    @Column(name="PerMonthAmount", length=10)
    private double perMonthAmount;
        
    @Column(name="OldPrice", length=10)
    private double oldPrice;
    
    @Column(name="Created_By", length=50)
    private String createdBy;
    
    @Column(name="FuelType", length=100)
    private String fuelType;
    
    @Column(name="Status", length=50)
    private String status;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CreationTime", length=30)
    private Date creationTime;

    @Transient
    int branchId;
    
    @Transient
    private String startDate;
    
    @Transient
    private String endDate;
    
	@Transient
	private int userId;	

    
    @ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;
    
    @ManyToOne
    @JoinColumn(name="fuelTypeId")
    private EFmFmVendorFuelContractTypeMasterPO eFmFmVendorFuelContractTypeMaster;
    
    
    
   
   
   

    public EFmFmFuelChargesPO() {
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




	public int getFuelChargeId() {
		return fuelChargeId;
	}



	public void setFuelChargeId(int fuelChargeId) {
		this.fuelChargeId = fuelChargeId;
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


	public double getNewPrice() {
		return newPrice;
	}


	public void setNewPrice(double newPrice) {
		this.newPrice = newPrice;
	}


	public double getOldPrice() {
		return oldPrice;
	}


	public void setOldPrice(double oldPrice) {
		this.oldPrice = oldPrice;
	}




	public double getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}


	public double getPerMonthAmount() {
		return perMonthAmount;
	}


	public void setPerMonthAmount(double perMonthAmount) {
		this.perMonthAmount = perMonthAmount;
	}


	
	



	


	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}


	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}


	public EFmFmVendorFuelContractTypeMasterPO geteFmFmVendorFuelContractTypeMaster() {
		return eFmFmVendorFuelContractTypeMaster;
	}


	public void seteFmFmVendorFuelContractTypeMaster(
			EFmFmVendorFuelContractTypeMasterPO eFmFmVendorFuelContractTypeMaster) {
		this.eFmFmVendorFuelContractTypeMaster = eFmFmVendorFuelContractTypeMaster;
	}


	public String getContractTitle() {
		return contractTitle;
	}


	public void setContractTitle(String contractTitle) {
		this.contractTitle = contractTitle;
	}


	public String getFuelType() {
		return fuelType;
	}


	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	
	
    
    
}