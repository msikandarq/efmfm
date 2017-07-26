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
 * The persistent class for the eFmFmEscortMaster database table.
 * 
 */
@Entity
@Table(name="eFmFmFieldAppConfigMaster")
public class EFmFmFieldAppConfigMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ConfigId", length=10)
	private int configId;

	@Column(name="ConfigDescription", length=250)
	private String configDescription;

	@Column(name="IsActive", length=10)
	private int IsActive;

	@Column(name="ConfigType", length=200)
	private String configType;

	@Transient
	private int userId;
	
	@Transient
	private String combinedFacility;
	
	//bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;

	public EFmFmFieldAppConfigMasterPO() {
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public String getConfigDescription() {
		return configDescription;
	}

	public void setConfigDescription(String configDescription) {
		this.configDescription = configDescription;
	}

	public int getIsActive() {
		return IsActive;
	}

	public void setIsActive(int isActive) {
		IsActive = isActive;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
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

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}

	
	
	
	
}