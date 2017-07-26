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
 * The persistent class for the eFmFmRouteMaster database table.
 * 
 */
@Entity
@Table(name="eFmFmDynamicVehicleCheckListPO")
public class EFmFmDynamicVehicleCheckListPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CheckListId", length=10)
	private int checkListId;
	
	@Column(name="CheckListName", length=255)
	private String checkListName;
	
	@Column(name="CheckListCoulum", length=255)
	private String checkListCoulum;
	
	@Column(name="CheckListType", length=255)
	private String checkListType;

	@Column(name="CreatedBy", length=50)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreationTime", length=100)
	private Date creationTime;
	
	@Column(name="Status", length=10)
	private String status;

	@Column(name="UpdatedBy", length=50)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UpdatedTime", length=30)
	private Date updatedTime;


	@Transient
	private int userId;	
	
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;


	
	public EFmFmDynamicVehicleCheckListPO() {
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
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCheckListId() {
		return checkListId;
	}

	public void setCheckListId(int checkListId) {
		this.checkListId = checkListId;
	}

	public String getCheckListName() {
		return checkListName;
	}

	public void setCheckListName(String checkListName) {
		this.checkListName = checkListName;
	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	public String getCheckListCoulum() {
		return checkListCoulum;
	}

	public void setCheckListCoulum(String checkListCoulum) {
		this.checkListCoulum = checkListCoulum;
	}

	public String getCheckListType() {
		return checkListType;
	}

	public void setCheckListType(String checkListType) {
		this.checkListType = checkListType;
	}

	

	

}