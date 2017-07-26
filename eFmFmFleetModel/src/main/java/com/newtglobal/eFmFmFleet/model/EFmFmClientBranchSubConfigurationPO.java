package com.newtglobal.eFmFmFleet.model;
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

/**
 * The persistent class for the EFmFmClientBranchConfiguration database table.
 * 
 */
@Entity
@Table(name="eFmFmClientBranchSubConfiguration")
public class EFmFmClientBranchSubConfigurationPO {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ClientBranchSubConfigurationId")
	private int clientBranchSubConfigurationId;

	@Column(name="SubModuleName", length=200)
	private String subModuleName;
	
	@Column(name="SubModuleDescription", length=200)
	private String subModuleDescription;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreationTime", length=100)
	private Date creationTime;
	

	//bi-directional many-to-one association to eFmFmClientBranchConfiguration
	@ManyToOne
	@JoinColumn(name="ClientBranchConfigurationId")
	private EFmFmClientBranchConfigurationPO eFmFmClientBranchConfiguration;
		
	
	public int getClientBranchSubConfigurationId() {
		return clientBranchSubConfigurationId;
	}

	public void setClientBranchSubConfigurationId(int clientBranchSubConfigurationId) {
		this.clientBranchSubConfigurationId = clientBranchSubConfigurationId;
	}

	public String getSubModuleName() {
		return subModuleName;
	}

	public void setSubModuleName(String subModuleName) {
		this.subModuleName = subModuleName;
	}

	public String getSubModuleDescription() {
		return subModuleDescription;
	}

	public void setSubModuleDescription(String subModuleDescription) {
		this.subModuleDescription = subModuleDescription;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public EFmFmClientBranchConfigurationPO geteFmFmClientBranchConfiguration() {
		return eFmFmClientBranchConfiguration;
	}

	public void seteFmFmClientBranchConfiguration(EFmFmClientBranchConfigurationPO eFmFmClientBranchConfiguration) {
		this.eFmFmClientBranchConfiguration = eFmFmClientBranchConfiguration;
	}
	
	}
