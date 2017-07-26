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
 * The persistent class for the EFmFmClientAreaMappingPO database table.
 * 
 */
@Entity
@Table(name="eFmFmClientAreaMapping")
public class EFmFmClientAreaMappingPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ClientAreaMappingId", length=15)
	private int clientAreaMappingId;
	
	
	@Column(name="FacilityWiseDistance", length=50)
	private double facilityWiseDistance;

	
	//bi-directional many-to-one association to eFmFmAreaMaster
	@ManyToOne
	@JoinColumn(name="AreaId")
	private EFmFmAreaMasterPO eFmFmAreaMaster;
	
	
	@Transient
	private int userId;	
	
	@Transient
	private String combinedFacility;


	//bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;

	public EFmFmClientAreaMappingPO() {
	}



	public int getClientAreaMappingId() {
		return clientAreaMappingId;
	}



	public void setClientAreaMappingId(int clientAreaMappingId) {
		this.clientAreaMappingId = clientAreaMappingId;
	}



	public EFmFmAreaMasterPO geteFmFmAreaMaster() {
		return eFmFmAreaMaster;
	}



	public void seteFmFmAreaMaster(EFmFmAreaMasterPO eFmFmAreaMaster) {
		this.eFmFmAreaMaster = eFmFmAreaMaster;
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



	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}



	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}



	public double getFacilityWiseDistance() {
		return facilityWiseDistance;
	}



	public void setFacilityWiseDistance(double facilityWiseDistance) {
		this.facilityWiseDistance = facilityWiseDistance;
	}



	
}