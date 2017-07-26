package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the eFmFmUserFacilityMapping database table.
 * 
 */
@Entity
@Table(name="eFmFmUserFacilityMapping")
public class EFmFmUserFacilityMappingPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="UserFacilityId", length=10)
	private int userFacilityId;

	//bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;

	//bi-directional many-to-one association to EFmFmUserMaster
	@ManyToOne
	@JoinColumn(name="UserId")
	private EFmFmUserMasterPO efmFmUserMaster;

	@Column(name="UserFacilityStatus", length=50)
	private String userFacilityStatus;
		
	@Transient
	private String employeeId;	
	
	@Transient
	private int userId;	
	
	@Transient
	private int branchId;	
	
	@Transient
	private String searchType;
	
	
	@Transient
	private String facilityIds;
	
	
	public EFmFmUserFacilityMappingPO() {
	}


	public int getUserFacilityId() {
		return userFacilityId;
	}


	public void setUserFacilityId(int userFacilityId) {
		this.userFacilityId = userFacilityId;
	}


	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}


	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}


	public EFmFmUserMasterPO getEfmFmUserMaster() {
		return efmFmUserMaster;
	}


	public void setEfmFmUserMaster(EFmFmUserMasterPO efmFmUserMaster) {
		this.efmFmUserMaster = efmFmUserMaster;
	}


	public String getUserFacilityStatus() {
		return userFacilityStatus;
	}


	public void setUserFacilityStatus(String userFacilityStatus) {
		this.userFacilityStatus = userFacilityStatus;
	}

	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public int getBranchId() {
		return branchId;
	}


	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}


	public String getEmployeeId() {
		return employeeId;
	}


	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}


	public String getSearchType() {
		return searchType;
	}


	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}


	public String getFacilityIds() {
		return facilityIds;
	}


	public void setFacilityIds(String facilityIds) {
		this.facilityIds = facilityIds;
	}

	
}