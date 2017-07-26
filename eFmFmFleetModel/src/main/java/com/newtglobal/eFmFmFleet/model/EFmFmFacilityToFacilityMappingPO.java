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
 * The persistent class for the FacilityToFacilityMappingPO database table.
 * 
 */
@Entity
@Table(name="eFmFmFacilityToFacilityMapping")
public class EFmFmFacilityToFacilityMappingPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="FacilityToFacilityId", length=10)
	private int facilityToFacilityId;

	//bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;

	
	@Column(name="BranchName", length=200)
	private String branchName;

	
	@Column(name="FacilityStatus", length=50)
	private String facilityStatus;
	
	
	@Column(name="BranchType", length=200)
	private String branchType;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FacilityStartDateTime", length = 50)
	private Date facilityStartDateTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FacilityEndDateTime", length = 50)
	private Date facilityEndDateTime;
	
	
	@Transient
	private int userId;	
	
	@Transient
	private int branchId;	
	
	@Transient
	private int employeeId;	

	public EFmFmFacilityToFacilityMappingPO() {
	}

	
	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFacilityStatus() {
		return facilityStatus;
	}

	public void setFacilityStatus(String facilityStatus) {
		this.facilityStatus = facilityStatus;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchType() {
		return branchType;
	}

	public void setBranchType(String branchType) {
		this.branchType = branchType;
	}

	public Date getFacilityStartDateTime() {
		return facilityStartDateTime;
	}

	public void setFacilityStartDateTime(Date facilityStartDateTime) {
		this.facilityStartDateTime = facilityStartDateTime;
	}

	public Date getFacilityEndDateTime() {
		return facilityEndDateTime;
	}

	public void setFacilityEndDateTime(Date facilityEndDateTime) {
		this.facilityEndDateTime = facilityEndDateTime;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}


	public int getFacilityToFacilityId() {
		return facilityToFacilityId;
	}


	public void setFacilityToFacilityId(int facilityToFacilityId) {
		this.facilityToFacilityId = facilityToFacilityId;
	}
	
	
	
}