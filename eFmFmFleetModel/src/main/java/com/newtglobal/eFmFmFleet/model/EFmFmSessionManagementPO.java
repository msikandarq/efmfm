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
import javax.persistence.Transient;

@Entity
@Table(name="eFmFmSessionManagement")
public class EFmFmSessionManagementPO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="SessionTableId",length=10)
	private int sessionTableId;
	
	@Column(name="SessionId" ,length=100)
	private String sessionId;
	
	@Column(name="UserAgent" ,length=1500)
	private String userAgent;
	
	@Column(name="SessionStartTime", length=30)
	private Date sessionStartTime;
	
	@Column(name="SessionEndTime", length=30)
	private Date sessionEndTime;
	
	@Column(name="UserIPAddress" ,length=50)
	private String userIPAddress;
	
	@Column(name="SessionActiveStatus" ,length=5)
	private String sessionActiveStatus;

	@Transient
	private String combinedFacility;
	
	public int getSessionTableId() {
		return sessionTableId;
	}

	public void setSessionTableId(int sessionTableId) {
		this.sessionTableId = sessionTableId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionID(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Date getSessionStartTime() {
		return sessionStartTime;
	}

	public void setSessionStartTime(Date sessionStartTime) {
		this.sessionStartTime = sessionStartTime;
	}

	public Date getSessionEndTime() {
		return sessionEndTime;
	}

	public void setSessionEndTime(Date sessionEndTime) {
		this.sessionEndTime = sessionEndTime;
	}

	public String getUserIPAddress() {
		return userIPAddress;
	}

	public void setUserIPAddress(String userIPAddress) {
		this.userIPAddress = userIPAddress;
	}

	public String getSessionActiveStatus() {
		return sessionActiveStatus;
	}

	public void setSessionActiveStatus(String sessionActiveStatus) {
		this.sessionActiveStatus = sessionActiveStatus;
	}
	
	 //bi-directional many-to-one association to eFmFmUserMaster
    @ManyToOne
    @JoinColumn(name="UserId")
    private EFmFmUserMasterPO efmFmUserMaster;
    
  //bi-directional many-to-one association to EFmFmClientBranchPO
    @ManyToOne
    @JoinColumn(name="BranchId")
    private EFmFmClientBranchPO eFmFmClientBranchPO;
  	
    
	
	public EFmFmUserMasterPO getEfmFmUserMaster() {
		return efmFmUserMaster;
	}

	public void setEfmFmUserMaster(EFmFmUserMasterPO efmFmUserMaster) {
		this.efmFmUserMaster = efmFmUserMaster;
	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	
	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}

	public EFmFmSessionManagementPO() {
	}
}
