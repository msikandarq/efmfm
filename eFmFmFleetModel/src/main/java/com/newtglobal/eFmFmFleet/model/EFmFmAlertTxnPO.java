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
 * The persistent class for the eFmFmAlertTxn database table.
 * 
 */
@Entity
@Table(name="eFmFmAlertTxn")
public class EFmFmAlertTxnPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="Id", length=15)
	private int id;

	@Column(name="Created_By", length=50)
	private 
	String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreationTime", length=30)
	private Date creationTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EndDate", length=30)
	private Date endDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="StartDate", length=30)
	private Date startDate;

	@Column(name="Status", length=10)
	private String status;
	
	@Column(name="AlertDescription", length=200)
	private String description;


	@Transient
	private String toDate;

	@Transient
	private String fromDate;
	

	@Transient
	private int userId;	

	
	@Column(name="UpdatedBy", length=50)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UpdatedTime", length=30)
	private Date updatedTime;

	@Column(name="UserType", length=50)
	private String userType;

	
	//bi-directional many-to-one association to EFmFm_Alert_Type_Master
	@ManyToOne
	@JoinColumn(name="AlertId")
	private EFmFmAlertTypeMasterPO efmFmAlertTypeMaster;
	
	//bi-directional many-to-one association to EFmFm_Alert_Type_Master
	@ManyToOne
	@JoinColumn(name="ZoneId")
	private EFmFmZoneMasterPO eFmFmZoneMaster;
	
	//bi-directional many-to-one association to EFmFm_User_Master
	@ManyToOne
	@JoinColumn(name="UserId")
	private EFmFmUserMasterPO efmFmUserMaster;



	public EFmFmAlertTxnPO() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public EFmFmAlertTypeMasterPO getEfmFmAlertTypeMaster() {
		return this.efmFmAlertTypeMaster;
	}

	public void setEfmFmAlertTypeMaster(EFmFmAlertTypeMasterPO efmFmAlertTypeMaster) {
		this.efmFmAlertTypeMaster = efmFmAlertTypeMaster;
	}
	

	public EFmFmZoneMasterPO geteFmFmZoneMaster() {
		return eFmFmZoneMaster;
	}

	public void seteFmFmZoneMaster(EFmFmZoneMasterPO eFmFmZoneMaster) {
		this.eFmFmZoneMaster = eFmFmZoneMaster;
	}

	public EFmFmUserMasterPO getEfmFmUserMaster() {
		return efmFmUserMaster;
	}

	public void setEfmFmUserMaster(EFmFmUserMasterPO efmFmUserMaster) {
		this.efmFmUserMaster = efmFmUserMaster;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	

}