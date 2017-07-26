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

@Entity
@Table(name = "AreaToClusterMapping")
public class AreaEmpClusterMappingPO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ClusterId", length = 15)
	private int clusterId;

	@Column(name = "Distance")
	private double distance;

	@Column(name = "ClusterName")
	private String clusterName;

	@Column(name = "IsClusterOregion")
	private boolean isClusterOregion;

	@Column(name = "ClusterToRouteStatus", length = 200)
	private boolean clusterToRouteStatus;

//	// bi-directional many-to-one association to EFmFmRoutingAreaPO
//	@ManyToOne
//	@JoinColumn(name = "RoutingAreaId")
//	private EFmFmRoutingAreaPO eFmFmRoutingArea;
//
	// bi-directional many-to-one association to EFmFmEmployeeTravelRequestPO
	@ManyToOne
	@JoinColumn(name = "requestId")
	private EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest;

	// bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name = "BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;
	
	// bidirectional manytoone association to eFmFmAlgoRoutes
	@OneToMany(mappedBy = "AreaEmpClusterMapping")
	private List<EFmFmAlgoRoutesPO> eFmFmAlgoRoutes;


	public int getClusterId() {
		return clusterId;
	}

	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public boolean isClusterOregion() {
		return isClusterOregion;
	}

	public void setClusterOregion(boolean isClusterOregion) {
		this.isClusterOregion = isClusterOregion;
	}

//	public EFmFmRoutingAreaPO geteFmFmRoutingArea() {
//		return eFmFmRoutingArea;
//	}
//
//	public void seteFmFmRoutingArea(EFmFmRoutingAreaPO eFmFmRoutingArea) {
//		this.eFmFmRoutingArea = eFmFmRoutingArea;
//	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public EFmFmEmployeeTravelRequestPO geteFmFmEmployeeTravelRequest() {
		return eFmFmEmployeeTravelRequest;
	}

	public void seteFmFmEmployeeTravelRequest(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest) {
		this.eFmFmEmployeeTravelRequest = eFmFmEmployeeTravelRequest;
	}

	public List<EFmFmAlgoRoutesPO> geteFmFmAlgoRoutes() {
		return eFmFmAlgoRoutes;
	}

	public void seteFmFmAlgoRoutes(List<EFmFmAlgoRoutesPO> eFmFmAlgoRoutes) {
		this.eFmFmAlgoRoutes = eFmFmAlgoRoutes;
	}

	public boolean isClusterToRouteStatus() {
		return clusterToRouteStatus;
	}

	public void setClusterToRouteStatus(boolean clusterToRouteStatus) {
		this.clusterToRouteStatus = clusterToRouteStatus;
	}

}
