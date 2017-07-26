package com.newtglobal.eFmFmFleet.model;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * The persistent class for the EFmFmClientBranchConfiguration database table.
 * 
 */
@Entity
@Table(name="eFmFmClientBranchConfiguration")
public class EFmFmClientBranchConfigurationPO {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ClientBranchConfigurationId")
	private int clientBranchConfigurationId;

	@Column(name="ModuleName", length=200)
	private String moduleName;
	
	@Column(name="ModuleDescription", length=200)
	private String moduleDescription;
	
	@Transient
	public  boolean isActive;
	
	@Transient
	public  boolean isAdminAccess;
	
    @Transient
    public int branchId;
    
    @Transient
    public int userId;
    
    
    @Transient
    public int profileId;
    
    @Transient
	private String combinedFacility;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreationTime", length=100)
	private Date creationTime;
	
	
	//bi-directional many-to-one association to eFmFmClientBranchConfigurationMapping
	@OneToMany(mappedBy="eFmFmClientBranchConfiguration")
	private List<EFmFmClientBranchConfigurationMappingPO> eFmFmClientBranchConfigurationMapping;
	
	//bi-directional many-to-one association to eFmFmClientBranchConfigurationMapping
	@OneToMany(mappedBy="eFmFmClientBranchConfiguration")
	private List<EFmFmClientBranchSubConfigurationPO> eFmFmClientBranchSubConfiguration;
	
	
	public int getClientBranchConfigurationId() {
		return clientBranchConfigurationId;
	}

	
	
	public void setClientBranchConfigurationId(int clientBranchConfigurationId) {
		this.clientBranchConfigurationId = clientBranchConfigurationId;
	}

	
	public List<EFmFmClientBranchConfigurationMappingPO> geteFmFmClientBranchConfigurationMapping() {
		return eFmFmClientBranchConfigurationMapping;
	}

	public void seteFmFmClientBranchConfigurationMapping(
			List<EFmFmClientBranchConfigurationMappingPO> eFmFmClientBranchConfigurationMapping) {
		this.eFmFmClientBranchConfigurationMapping = eFmFmClientBranchConfigurationMapping;
	}

	public String getModuleName() {
		return moduleName;
	}



	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}



	public String getModuleDescription() {
		return moduleDescription;
	}


	public void setModuleDescription(String moduleDescription) {
		this.moduleDescription = moduleDescription;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public List<EFmFmClientBranchSubConfigurationPO> geteFmFmClientBranchSubConfiguration() {
		return eFmFmClientBranchSubConfiguration;
	}

	public void seteFmFmClientBranchSubConfiguration(
			List<EFmFmClientBranchSubConfigurationPO> eFmFmClientBranchSubConfiguration) {
		this.eFmFmClientBranchSubConfiguration = eFmFmClientBranchSubConfiguration;
	}



	public boolean isActive() {
		return isActive;
	}



	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}



	public int getBranchId() {
		return branchId;
	}



	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}



	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}



	public boolean isAdminAccess() {
		return isAdminAccess;
	}



	public void setAdminAccess(boolean isAdminAccess) {
		this.isAdminAccess = isAdminAccess;
	}



	public int getProfileId() {
		return profileId;
	}



	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}



	public String getCombinedFacility() {
		return combinedFacility;
	}



	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}
	
}

