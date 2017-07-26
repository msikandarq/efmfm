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
 * The persistent class for the eFmFmVendorContractTypeMaster database table.
 * 
 */
@Entity
@Table(name="eFmFmVendorContractTypeMaster")
public class EFmFmVendorContractTypeMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ContractTypeId", length=15)
	private int contractTypeId;
	
	@Column(name="ContractStatus", length=10)
	private String contractStatus;
	
	@Column(name="ContractType", length=50)
	private String contractType;
	
	@Column(name="ContractDescription", length=50)
	private String contractDescription;
	
	@Column(name="ServiceTax", length=10)
	private double serviceTax;	
	
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

	@Transient
	private int userId;
	
	@Transient
	private String editOptionRequired;
	//bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;
	
	/*//bi-directional many-to-one association to EFmFmVehicleMaster
	@OneToMany(mappedBy="eFmFmVendorContractTypeMaster")
	private List<EFmFmVehicleMasterPO> efmFmVehicleMasters;*/
	
	
	//bidirectional manytoone association to eFmFmVendorContractMaster
	@OneToMany(mappedBy="eFmFmVendorContractTypeMaster")
	private List<EFmFmFixedDistanceContractDetailPO> eFmFmFixedDistanceContractDetail;
		
	//bidirectional manytoone association to eFmFmVendorContractMaster
	@OneToMany(mappedBy="eFmFmVendorContractTypeMaster")
	private List<EFmFmTripBasedContractDetailPO> eFmFmTripBasedContractDetail;
			
	
	

	public EFmFmVendorContractTypeMasterPO() {
	}

	public int getContractTypeId() {
		return contractTypeId;
	}

	public void setContractTypeId(int contractTypeId) {
		this.contractTypeId = contractTypeId;
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

	/*public List<EFmFmVehicleMasterPO> getEfmFmVehicleMasters() {
		return efmFmVehicleMasters;
	}

	public void setEfmFmVehicleMasters(
			List<EFmFmVehicleMasterPO> efmFmVehicleMasters) {
		this.efmFmVehicleMasters = efmFmVehicleMasters;
	}
*/
	public double getServiceTax() {
		return serviceTax;
	}

	public void setServiceTax(double serviceTax) {
		this.serviceTax = serviceTax;
	}

	public List<EFmFmFixedDistanceContractDetailPO> geteFmFmFixedDistanceContractDetail() {
		return eFmFmFixedDistanceContractDetail;
	}

	public void seteFmFmFixedDistanceContractDetail(
			List<EFmFmFixedDistanceContractDetailPO> eFmFmFixedDistanceContractDetail) {
		this.eFmFmFixedDistanceContractDetail = eFmFmFixedDistanceContractDetail;
	}

	public List<EFmFmTripBasedContractDetailPO> geteFmFmTripBasedContractDetail() {
		return eFmFmTripBasedContractDetail;
	}

	public void seteFmFmTripBasedContractDetail(
			List<EFmFmTripBasedContractDetailPO> eFmFmTripBasedContractDetail) {
		this.eFmFmTripBasedContractDetail = eFmFmTripBasedContractDetail;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEditOptionRequired() {
		return editOptionRequired;
	}

	public void setEditOptionRequired(String editOptionRequired) {
		this.editOptionRequired = editOptionRequired;
	}

	
}