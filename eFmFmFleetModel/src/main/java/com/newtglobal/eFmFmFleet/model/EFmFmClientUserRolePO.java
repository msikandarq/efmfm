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
 * The persistent class for the eFmFmClientUserRole database table.
 * 
 */
@Entity
@Table(name="eFmFmClientUserRole")
public class EFmFmClientUserRolePO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="UserRoleId", length=10)
	private int userRoleId;

	//bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;

	//bi-directional many-to-one association to EFmFmUserMaster
	@ManyToOne
	@JoinColumn(name="UserId")
	private EFmFmUserMasterPO efmFmUserMaster;

	//bi-directional many-to-one association to EFmFmRoleMaster
	@ManyToOne
	@JoinColumn(name="RoleId")
	private EFmFmRoleMasterPO efmFmRoleMaster;
	
	
	//bi-directional many-to-one association to EFmFmRoleMaster
	@ManyToOne
	@JoinColumn(name="ClientBranchSubConfigurationId")
	private EFmFmClientBranchSubConfigurationPO eFmFmClientBranchSubConfiguration;
		
	
	@Transient
	int moduleId;
	
	@Transient
	private String combinedFacility;
	

	@Transient
	private int userId;	

	public EFmFmClientUserRolePO() {
	}

	

	public int getUserRoleId() {
		return userRoleId;
	}



	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
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

	public EFmFmRoleMasterPO getEfmFmRoleMaster() {
		return this.efmFmRoleMaster;
	}

	public void setEfmFmRoleMaster(EFmFmRoleMasterPO efmFmRoleMaster) {
		this.efmFmRoleMaster = efmFmRoleMaster;
	}

	public int getModuleId() {
		return moduleId;
	}



	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}



	public EFmFmClientBranchSubConfigurationPO geteFmFmClientBranchSubConfiguration() {
		return eFmFmClientBranchSubConfiguration;
	}



	public void seteFmFmClientBranchSubConfiguration(
			EFmFmClientBranchSubConfigurationPO eFmFmClientBranchSubConfiguration) {
		this.eFmFmClientBranchSubConfiguration = eFmFmClientBranchSubConfiguration;
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