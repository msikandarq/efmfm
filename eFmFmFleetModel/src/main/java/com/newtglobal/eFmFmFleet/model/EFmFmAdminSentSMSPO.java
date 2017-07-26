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
import javax.persistence.Transient;

@Entity
@Table(name = "eFmFmAdminSentSMS")
public class EFmFmAdminSentSMSPO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3423694312446575622L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MessageId", length = 10)
	private int messageId;
	
	@Column(name = "MessageType", length = 10)
	private String messageType;
	
	@Column(name = "MessageDescription", length = 250)
	private String messageDescription;
	
	@Column(name = "CreatedBy", length = 10)
	private String createdBy;
	
	@Column(name = "NotificationType", length = 20)
	private String notificationType;
	
	@Transient
	private String combinedFacility;
	
	
	@Transient
	private String employeeId;

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}




	@Column(name = "MsgSentDate")
	private Date msgSentDate;
	
	@Transient
	private String shiftDate;
	
	public String getShiftDate() {
		return shiftDate;
	}

	public void setShiftDate(String shiftDate) {
		this.shiftDate = shiftDate;
	}




	@Column(name = "CustMsgId", length = 10)
	private String custMsgId;
	
	@Column(name = "MobileNumber", length = 20)
	private String mobileNumber;
	
	@Column(name = "TripType", length = 10)
	private String tripType;
	
	@Column(name = "ShiftTime", length = 10)
	private String shiftTime;
	
	@Column(name = "Status", length = 10)
	private String status;
	
	
	@Transient
	private List<String> mobileNumbers;	
	
	public List<String> getMobileNumbers() {
		return mobileNumbers;
	}

	public void setMobileNumbers(List<String> mobileNumbers) {
		this.mobileNumbers = mobileNumbers;
	}
	
	
		

	@Transient
	private List<String> employeeIds;
	
	
	public List<String> getEmployeeIds() {
		return employeeIds;
	}

	public void setEmployeeIds(List<String> employeeIds) {
		this.employeeIds = employeeIds;
	}

	// bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name = "BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;

	// bi-directional many-to-one association to EFmFmUserMaster
	@ManyToOne
	@JoinColumn(name = "UserId")
	private EFmFmUserMasterPO efmFmUserMaster;
	
	
	
	
	
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

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	
	

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageDescription() {
		return messageDescription;
	}

	public void setMessageDescription(String messageDescription) {
		this.messageDescription = messageDescription;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getMsgSentDate() {
		return msgSentDate;
	}

	public void setMsgSentDate(Date msgSentDate) {
		this.msgSentDate = msgSentDate;
	}

	public String getCustMsgId() {
		return custMsgId;
	}

	public void setCustMsgId(String custMsgId) {
		this.custMsgId = custMsgId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getShiftTime() {
		return shiftTime;
	}

	public void setShiftTime(String shiftTime) {
		this.shiftTime = shiftTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	
	
}
