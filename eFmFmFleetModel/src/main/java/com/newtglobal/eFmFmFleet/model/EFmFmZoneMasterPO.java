package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
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
 * The persistent class for the eFmFmRouteMaster database table.
 * 
 */
@Entity
@Table(name="eFmFmZoneMaster")
public class EFmFmZoneMasterPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ZoneId", length=10)
	private int zoneId;
	
	@Column(name="ZoneName", length=255)
	private String zoneName;


	@Column(name="CreatedBy", length=50)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreationTime", length=100)
	private Date creationTime;

	
	@Column(name="Status", length=10)
	private String status;
		
	@Column(name = "IsNodalRoute")
	boolean isNodalRoute;

	@Column(name="UpdatedBy", length=50)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UpdatedTime", length=30)
	private Date updatedTime;

	@Transient
	int branchId;
	
	@Transient
	int routeId;
	
	@Transient
	private int userId;	
	
	@Transient
	private String combinedFacility;
	
	//bi-directional many-to-one association to EFmFmClientRouteAreaMapping
	@OneToMany(mappedBy="eFmFmZoneMaster")
	private List<EFmFmRouteAreaMappingPO> efmFmRouteAreaMappings;

	//bi-directional many-to-one association to EFmFmClientRouteMapping
	@OneToMany(mappedBy="eFmFmZoneMaster")
	private List<EFmFmClientRouteMappingPO> efmFmClientRouteMappings;
	
	//bi-directional many-to-one association to EFmFmAlertTxn
	@OneToMany(mappedBy="eFmFmZoneMaster")
	private List<EFmFmAlertTxnPO> efmFmAlertTxns;


	
	public EFmFmZoneMasterPO() {
	}

	

	public int getZoneId() {
		return zoneId;
	}



	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}



	public String getZoneName() {
		return zoneName;
	}



	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}



	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}


	public List<EFmFmRouteAreaMappingPO> getEfmFmRouteAreaMappings() {
		return efmFmRouteAreaMappings;
	}

	public void setEfmFmRouteAreaMappings(
			List<EFmFmRouteAreaMappingPO> efmFmRouteAreaMappings) {
		this.efmFmRouteAreaMappings = efmFmRouteAreaMappings;
	}

	public List<EFmFmClientRouteMappingPO> getEfmFmClientRouteMappings() {
		return this.efmFmClientRouteMappings;
	}

	public void setEfmFmClientRouteMappings(List<EFmFmClientRouteMappingPO> efmFmClientRouteMappings) {
		this.efmFmClientRouteMappings = efmFmClientRouteMappings;
	}

	public List<EFmFmAlertTxnPO> getEfmFmAlertTxns() {
		return efmFmAlertTxns;
	}

	public void setEfmFmAlertTxns(List<EFmFmAlertTxnPO> efmFmAlertTxns) {
		this.efmFmAlertTxns = efmFmAlertTxns;
	}



	public boolean isNodalRoute() {
		return isNodalRoute;
	}



	public void setNodalRoute(boolean isNodalRoute) {
		this.isNodalRoute = isNodalRoute;
	}



	public int getBranchId() {
		return branchId;
	}



	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}



	public int getRouteId() {
		return routeId;
	}



	public void setRouteId(int routeId) {
		this.routeId = routeId;
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