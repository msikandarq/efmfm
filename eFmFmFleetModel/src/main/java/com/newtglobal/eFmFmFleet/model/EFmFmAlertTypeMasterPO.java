package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the eFmFmAlertTypeMaster database table.
 * 
 */
@Entity
@Table(name="efmFmAlertTypeMaster")
public class EFmFmAlertTypeMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="AlertId", length=15)
	private int alertId;

	@Column(name="AlertDescription", length=100)
	private String alertDescription;
	
	@Column(name="AlertTitle", length=100)
	private String alertTitle;
	
	@Column(name="AlertStatus", length=100)
	private String status;

	@Column(name="AlertType", length=50)
	private String alertType;
	
	@Transient
	private int userId;	


	//bi-directional many-to-one association to EFmFm_Alert_Txn
	@OneToMany(mappedBy="efmFmAlertTypeMaster")
	private List<EFmFmAlertTxnPO> efmFmAlertTxns;

	
	//bi-directional many-to-one association to eFmFmTripAlerts
	@OneToMany(mappedBy="efmFmAlertTypeMaster")
	private List<EFmFmTripAlertsPO> eFmFmTripAlerts;

	//bi-directional many-to-one association to EFmFmClientMaster
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;

	//bi-directional many-to-one association to EFmFm_User_Master
	@ManyToOne
	@JoinColumn(name="UserId")
	private EFmFmUserMasterPO efmFmUserMaster;
	
	public EFmFmAlertTypeMasterPO() {
	}

	public int getAlertId() {
		return alertId;
	}



	public void setAlertId(int alertId) {
		this.alertId = alertId;
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



	public List<EFmFmAlertTxnPO> getEfmFmAlertTxns() {
		return this.efmFmAlertTxns;
	}

	public void setEfmFmAlertTxns(List<EFmFmAlertTxnPO> efmFmAlertTxns) {
		this.efmFmAlertTxns = efmFmAlertTxns;
	}

	public EFmFmAlertTxnPO addEfmFmAlertTxn(EFmFmAlertTxnPO efmFmAlertTxn) {
		getEfmFmAlertTxns().add(efmFmAlertTxn);
		efmFmAlertTxn.setEfmFmAlertTypeMaster(this);

		return efmFmAlertTxn;
	}

	public EFmFmAlertTxnPO removeEfmFmAlertTxn(EFmFmAlertTxnPO efmFmAlertTxn) {
		getEfmFmAlertTxns().remove(efmFmAlertTxn);
		efmFmAlertTxn.setEfmFmAlertTypeMaster(null);

		return efmFmAlertTxn;
	}

	
	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	public EFmFmUserMasterPO getEfmFmUserMaster() {
		return this.efmFmUserMaster;
	}

	public void setEfmFmUserMaster(EFmFmUserMasterPO efmFmUserMaster) {
		this.efmFmUserMaster = efmFmUserMaster;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<EFmFmTripAlertsPO> geteFmFmTripAlerts() {
		return eFmFmTripAlerts;
	}

	public void seteFmFmTripAlerts(List<EFmFmTripAlertsPO> eFmFmTripAlerts) {
		this.eFmFmTripAlerts = eFmFmTripAlerts;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	

	
}