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
 * The persistent class for the eFmFmSmsAlertMaster database table.
 * 
 */
@Entity
@Table(name="eFmFmSmsAlertMaster")
public class EFmFmSmsAlertMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TriggerId", length=15)
	private int triggerId;
	
	@Column(name="MobileNumber", length=200)
	private String mobileNumber;
	
	@Column(name="EmailId", length=200)
	private String emailId;

	@Column(name="AlertDescription", length=250)
	private String alertDescription;	
			
	@Column(name="AlertTitle", length=100)
	private String alertTitle;	
	
	@Column(name="SmsAlertStatus", length=100)
	private String smsAlertStatus;
	
	@Column(name="EmailAlertStatus", length=100)
	private String emailAlertStatus;	
	
	@Column(name="UserType", length=100)
	private String userType;	
	
	@Column(name="UniqueId", length=10)
	private Integer uniqueId;	

	@Column(name="AlertType", length=50)
	private String alertType;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedDate", length = 50)
	private Date createdDate;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SMSTriggerDate", length = 50)
	private Date smsTriggerDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SMSGateWayTriggerDate", length = 50)
	private Date sMSGateWayTriggerDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SMS_Sender_Id", length = 50)
	private Date smsSenderId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EmailTriggerDate", length = 50)
	private Date EmailTriggerDate;	
	
	@Transient
	private int userId;	

	//bi-directional many-to-one association to EFmFmClientMaster
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;
	
	public EFmFmSmsAlertMasterPO() {
	}
	
	public String getAlertDescription() {
		return alertDescription;
	}

	public void setAlertDescription(String alertDescription) {
		this.alertDescription = alertDescription;
	}

	public String getAlertTitle() {
		return alertTitle;
	}

	public void setAlertTitle(String alertTitle) {
		this.alertTitle = alertTitle;
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	
	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	public int getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(int triggerId) {
		this.triggerId = triggerId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getSmsAlertStatus() {
		return smsAlertStatus;
	}

	public void setSmsAlertStatus(String smsAlertStatus) {
		this.smsAlertStatus = smsAlertStatus;
	}

	public String getEmailAlertStatus() {
		return emailAlertStatus;
	}

	public void setEmailAlertStatus(String emailAlertStatus) {
		this.emailAlertStatus = emailAlertStatus;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Integer getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(Integer uniqueId) {
		this.uniqueId = uniqueId;
	}

	

	public Date getSmsTriggerDate() {
		return smsTriggerDate;
	}

	public void setSmsTriggerDate(Date smsTriggerDate) {
		this.smsTriggerDate = smsTriggerDate;
	}

	public Date getsMSGateWayTriggerDate() {
		return sMSGateWayTriggerDate;
	}

	public void setsMSGateWayTriggerDate(Date sMSGateWayTriggerDate) {
		this.sMSGateWayTriggerDate = sMSGateWayTriggerDate;
	}

	public Date getSmsSenderId() {
		return smsSenderId;
	}

	public void setSmsSenderId(Date smsSenderId) {
		this.smsSenderId = smsSenderId;
	}

	public Date getEmailTriggerDate() {
		return EmailTriggerDate;
	}

	public void setEmailTriggerDate(Date emailTriggerDate) {
		EmailTriggerDate = emailTriggerDate;
	}

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
	
}