package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
import java.sql.Time;
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
 * The persistent class for the eFmFmNodalAreaMaster database table.
 * 
 */

@Entity
@Table(name="eFmFmNodalAreaMaster")
public class EFmFmAreaNodalMasterPO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="NodalPointId", length=15)
	private int nodalPointId;
	
	@Column(name="NodalPointName", length=200)
	private String nodalPointName;


	@Column(name="NodalPointDescription", length=200)
	private String nodalPointDescription;
	
	@Column(name="NodalPoints", length=200)
	private String nodalPoints;
	
	@Column(name="NodalPointsAddress", length=250)
	private String nodalPointsAddress;
	
	@Column(name="NodalPointTime", length=100)
	private Time nodalPointTime;
	
	@Column(name="NodalPointFlag", length=11)
	private String nodalPointFlg;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreationTime", length=100)
	private Date creationTime;


	@Transient
	String routeName;
	
	@Transient
	String areaName;
	
	@Transient
	int branchId;
	
	@Transient
	private int userId;	
	
	@Transient
	private String combinedFacility;
		
	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	//bi-directional many-to-one association to eFmFmRouteAreaMapping
	@OneToMany(mappedBy="eFmFmNodalAreaMaster")
	private List<EFmFmRouteAreaMappingPO> eFmFmRouteAreaMapping;


	public int getNodalPointId() {
		return nodalPointId;
	}

	public void setNodalPointId(int nodalPointId) {
		this.nodalPointId = nodalPointId;
	}

	public String getNodalPointName() {
		return nodalPointName;
	}

	public void setNodalPointName(String nodalPointName) {
		this.nodalPointName = nodalPointName;
	}

	public String getNodalPointDescription() {
		return nodalPointDescription;
	}

	public void setNodalPointDescription(String nodalPointDescription) {
		this.nodalPointDescription = nodalPointDescription;
	}

	public String getNodalPoints() {
		return nodalPoints;
	}

	public void setNodalPoints(String nodalPoints) {
		this.nodalPoints = nodalPoints;
	}

	public String getNodalPointsAddress() {
		return nodalPointsAddress;
	}

	public void setNodalPointsAddress(String nodalPointsAddress) {
		this.nodalPointsAddress = nodalPointsAddress;
	}

	public List<EFmFmRouteAreaMappingPO> geteFmFmRouteAreaMapping() {
		return eFmFmRouteAreaMapping;
	}

	public void seteFmFmRouteAreaMapping(
			List<EFmFmRouteAreaMappingPO> eFmFmRouteAreaMapping) {
		this.eFmFmRouteAreaMapping = eFmFmRouteAreaMapping;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
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

	public Time getNodalPointTime() {
		return nodalPointTime;
	}

	public void setNodalPointTime(Time nodalPointTime) {
		this.nodalPointTime = nodalPointTime;
	}

	public String getNodalPointFlg() {
		return nodalPointFlg;
	}

	public void setNodalPointFlg(String nodalPointFlg) {
		this.nodalPointFlg = nodalPointFlg;
	}

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	
	

}
