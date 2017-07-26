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
@Table(name = "eFmFmAdminCustomMessage")
public class EFmFmAdminCustomMessagePO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CustMessageId", length = 10)
	private int messageId;

		
	@Column(name = "CustMsgDescription", length = 250)
	private String custMsgDescription;
	
	@Column(name = "IsActive", length = 10)
	private String isActive;
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "VisibleUser")
	private String visibleUser;
	
	@Transient
	private String combinedFacility;
	
	
	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	
	public String getCustMsgDescription() {
		return custMsgDescription;
	}

	public void setCustMsgDescription(String custMsgDescription) {
		this.custMsgDescription = custMsgDescription;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getVisibleUser() {
		return visibleUser;
	}

	public void setVisibleUser(String visibleUser) {
		this.visibleUser = visibleUser;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	@Transient
	private int branchId;
	
	
	// bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name = "BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;

	// bi-directional many-to-one association to EFmFmUserMaster
	@ManyToOne
	@JoinColumn(name = "UserId")
	private EFmFmUserMasterPO efmFmUserMaster;
	
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

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}

	

	
	
	

}
