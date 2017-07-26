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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * The persistent class for the EFmFmVehicleCapacityMasterPO database table.
 * 
 */
@Entity
@Table(name="eFmFmVehicleCapacityMaster")
public class EFmFmVehicleCapacityMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VehicleCapacityId", length=10)
	private int vehicleCapacityId;

	@Column(name="Capacity", length=10)
	private int capacity;

	
	@Column(name="Status", length=10)
	private String status;
	
	@Column(name="VehicleType", length=100)
	private String vehicleType;
	
	@Column(name="AvailableSeat", length=10)
	private int availableSeat;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreationDate", length=30)
	private Date creationDate;
			
	@Column(name="Remarks", length=250)
	private String remarks;
	
	@Column(name="UpdatedBy", length=50)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UpdatedTime", length=30)
	private Date updatedTime;

	@Transient
	private int userId;		
	
	@Transient
	private int branchId;		
	
	@Transient
	private String combinedFacility;
	
	@Transient
	private int selectCapacity;		

	
	
	
	// bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name = "BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;
	
	
	
	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}
	


	public EFmFmVehicleCapacityMasterPO() {
	}

	public int getVehicleCapacityId() {
		return vehicleCapacityId;
	}

	public void setVehicleCapacityId(int vehicleCapacityId) {
		this.vehicleCapacityId = vehicleCapacityId;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public int getAvailableSeat() {
		return availableSeat;
	}

	public void setAvailableSeat(int availableSeat) {
		this.availableSeat = availableSeat;
	}

	

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	public int getSelectCapacity() {
		return selectCapacity;
	}

	public void setSelectCapacity(int selectCapacity) {
		this.selectCapacity = selectCapacity;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	
	
}