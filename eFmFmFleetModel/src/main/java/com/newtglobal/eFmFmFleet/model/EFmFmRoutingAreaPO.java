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

@Entity
@Table(name = "RoutingAreas")
public class EFmFmRoutingAreaPO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RoutingAreaId", length = 15)
	private int routingAreaId;
	
	@Column(name = "Distance")
	private double distance;
	
	@Column(name = "RoutingAreaName")
	private String routingAreaName;

	@Column(name = "IsOregion")
	private boolean isOregion;
	
	@Column(name = "EmpsAreaToCluster")
	private boolean empsAreaToCluster;

	@Column(name = "RoutingAreaStatus", length = 200)
	private String routingAreaStatus;

//	// bi-directional many-to-one association to EFmFmUserMaster
//	@ManyToOne
//	@JoinColumn(name = "UserId")
//	private EFmFmUserMasterPO efmFmUserMaster;
	
	//bi-directional many-to-one association to EFmFmEmployeeTravelRequestPO
	@ManyToOne
	@JoinColumn(name="requestId")
	private EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest;

	// bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name = "BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;
	
//	//bidirectional manytoone association to areaEmpClusterMapping
//	@OneToMany(mappedBy="eFmFmRoutingArea")
//	private List<AreaEmpClusterMappingPO> areaEmpClusterMapping;



	public int getRoutingAreaId() {
		return routingAreaId;
	}

	public void setRoutingAreaId(int routingAreaId) {
		this.routingAreaId = routingAreaId;
	}

	public String getRoutingAreaName() {
		return routingAreaName;
	}

	public void setRoutingAreaName(String routingAreaName) {
		this.routingAreaName = routingAreaName;
	}

	public boolean isOregion() {
		return isOregion;
	}

	public void setOregion(boolean isOregion) {
		this.isOregion = isOregion;
	}

	public String getRoutingAreaStatus() {
		return routingAreaStatus;
	}

	public void setRoutingAreaStatus(String routingAreaStatus) {
		this.routingAreaStatus = routingAreaStatus;
	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public boolean isEmpsAreaToCluster() {
		return empsAreaToCluster;
	}

	public void setEmpsAreaToCluster(boolean empsAreaToCluster) {
		this.empsAreaToCluster = empsAreaToCluster;
	}

//	public List<AreaEmpClusterMappingPO> getAreaEmpClusterMapping() {
//		return areaEmpClusterMapping;
//	}
//
//	public void setAreaEmpClusterMapping(List<AreaEmpClusterMappingPO> areaEmpClusterMapping) {
//		this.areaEmpClusterMapping = areaEmpClusterMapping;
//	}

	public EFmFmEmployeeTravelRequestPO geteFmFmEmployeeTravelRequest() {
		return eFmFmEmployeeTravelRequest;
	}

	public void seteFmFmEmployeeTravelRequest(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest) {
		this.eFmFmEmployeeTravelRequest = eFmFmEmployeeTravelRequest;
	}


	
}
