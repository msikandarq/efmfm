package com.newtglobal.eFmFmFleet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="eFmFmClientBranchConfigurationMapping")
public class EFmFmClientBranchConfigurationMappingPO {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ClientBranchConfigurationMappingId")
	private int clientBranchConfigurationMappingId;
	

	//bi-directional many-to-one association to eFmFmClientBranchConfiguration
	@ManyToOne
	@JoinColumn(name="ClientBranchConfigurationId")
	private EFmFmClientBranchConfigurationPO eFmFmClientBranchConfiguration;
		
	
	//bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;

	public int getClientBranchConfigurationMappingId() {
		return clientBranchConfigurationMappingId;
	}

	public void setClientBranchConfigurationMappingId(int clientBranchConfigurationMappingId) {
		this.clientBranchConfigurationMappingId = clientBranchConfigurationMappingId;
	}

	public EFmFmClientBranchConfigurationPO geteFmFmClientBranchConfiguration() {
		return eFmFmClientBranchConfiguration;
	}

	public void seteFmFmClientBranchConfiguration(EFmFmClientBranchConfigurationPO eFmFmClientBranchConfiguration) {
		this.eFmFmClientBranchConfiguration = eFmFmClientBranchConfiguration;
	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

}
