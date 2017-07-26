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


/**
 * The persistent class for the eFmFmVendorContractTypeMaster database table.
 * 
 */
@Entity
@Table(name="eFmFmVendorFuelContractTypeMaster")
public class EFmFmVendorFuelContractTypeMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="FuelTypeId", length=15)
	private int fuelTypeId;
	
	@Column(name="ContractStatus", length=10)
	private String contractStatus;
	
	@Column(name="ContractType", length=50)
	private String contractType;
	
	@Column(name="ContractDescription", length=50)
	private String contractDescription;
	
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

	//bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;
	
	
	@OneToMany(mappedBy="eFmFmVendorFuelContractTypeMaster")
    private List<EFmFmFuelChargesPO> eFmFmFuelCharge;
	
	
	
	
	@OneToMany(mappedBy="eFmFmVendorFuelContractTypeMaster")
	private List<EFmFmFixedDistanceContractDetailPO> eFmFmFixedDistanceContractDetail;
	
	

	public EFmFmVendorFuelContractTypeMasterPO() {
	}

	

	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getContractDescription() {
		return contractDescription;
	}

	public void setContractDescription(String contractDescription) {
		this.contractDescription = contractDescription;
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



	public int getFuelTypeId() {
		return fuelTypeId;
	}



	public List<EFmFmFuelChargesPO> geteFmFmFuelCharge() {
		return eFmFmFuelCharge;
	}



	public void setFuelTypeId(int fuelTypeId) {
		this.fuelTypeId = fuelTypeId;
	}



	public void seteFmFmFuelCharge(List<EFmFmFuelChargesPO> eFmFmFuelCharge) {
		this.eFmFmFuelCharge = eFmFmFuelCharge;
	}



	public List<EFmFmFixedDistanceContractDetailPO> geteFmFmFixedDistanceContractDetail() {
		return eFmFmFixedDistanceContractDetail;
	}



	public void seteFmFmFixedDistanceContractDetail(
			List<EFmFmFixedDistanceContractDetailPO> eFmFmFixedDistanceContractDetail) {
		this.eFmFmFixedDistanceContractDetail = eFmFmFixedDistanceContractDetail;
	}

	
}