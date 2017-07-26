package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.sql.Time;
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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The persistent class for the eFmFmSmsAlertMaster database table.
 * 
 */
@Entity
@Table(name="eFmFmIndicationMasterPO")
public class EFmFmIndicationMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="IndicationId", length=15)
	private int indicationId;
	
	@Column(name="AlertTypeRequest", length=200)
	private String alertTypeRequest;

			
	@Column(name="AlertFunctionlityType", length=50)
	private String alertFunctionlityType;
	
	@Column(name="LevelType", length=100)
	private String levelType;	
		
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedDate", length = 50)
	private Date createdDate;	
	
	@Column(name="IsActive", length=100)
	private String isActive;
	
	@Column(name="TiggerTime", length=30)
	private Time tiggerTime;
	
	@Transient
	private int userId;	
	
	@Transient
	private String time;	
	
	@Transient
	@JsonProperty("LevelStatusValues")
	private List<EFmFmIndicationMasterPO> levelStatus;	
	

	public EFmFmIndicationMasterPO() {
	}
	
	@ManyToOne
	@JoinColumn(name="UserId")
	private EFmFmUserMasterPO efmFmUserMaster;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getIndicationId() {
		return indicationId;
	}

	public void setIndicationId(int indicationId) {
		this.indicationId = indicationId;
	}

	public String getAlertFunctionlityType() {
		return alertFunctionlityType;
	}

	public void setAlertFunctionlityType(String alertFunctionlityType) {
		this.alertFunctionlityType = alertFunctionlityType;
	}	

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public EFmFmUserMasterPO getEfmFmUserMaster() {
		return efmFmUserMaster;
	}

	public void setEfmFmUserMaster(EFmFmUserMasterPO efmFmUserMaster) {
		this.efmFmUserMaster = efmFmUserMaster;
	}

	public String getAlertTypeRequest() {
		return alertTypeRequest;
	}

	public void setAlertTypeRequest(String alertTypeRequest) {
		this.alertTypeRequest = alertTypeRequest;
	}

	public String getLevelType() {
		return levelType;
	}

	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}

	public List<EFmFmIndicationMasterPO> getLevelStatus() {
		return levelStatus;
	}

	public void setLevelStatus(List<EFmFmIndicationMasterPO> levelStatus) {
		this.levelStatus = levelStatus;
	}

	
	public Time getTiggerTime() {
		return tiggerTime;
	}

	public void setTiggerTime(Time tiggerTime) {
		this.tiggerTime = tiggerTime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @param indicationId
	 * @param alertTypeRequest
	 * @param alertFunctionlityType
	 * @param levelType
	 * @param userId
	 * @param time
	 */
	public EFmFmIndicationMasterPO(int indicationId, String alertTypeRequest, String alertFunctionlityType,
			String levelType, int userId, String time) {
		super();
		this.indicationId = indicationId;
		this.alertTypeRequest = alertTypeRequest;
		this.alertFunctionlityType = alertFunctionlityType;
		this.levelType = levelType;
		this.userId = userId;
		this.time = time;
	}

	
	
}