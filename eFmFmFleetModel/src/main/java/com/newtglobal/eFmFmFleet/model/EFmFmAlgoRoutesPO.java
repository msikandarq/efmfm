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
@Table(name = "eFmFmAlgoRoutes")
public class EFmFmAlgoRoutesPO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "AlgoRouteId", length = 15)
	private int algoRouteId;

	@Column(name = "Distance")
	private double distance;

	@Column(name = "RouteName")
	private String routeName;
	
	@Column(name = "vehicleType")
	private int vehicleType;

	@Column(name = "algoRouteOregion")
	private boolean algoRouteOregion;

	@Column(name = "RouteAllocationStatus", length = 200)
	private boolean routeAllocationStatus;

	// bi-directional many-to-one association to EFmFmRoutingAreaPO
	@ManyToOne
	@JoinColumn(name = "clusterId")
	private AreaEmpClusterMappingPO AreaEmpClusterMapping;

	// bi-directional many-to-one association to EFmFmEmployeeTravelRequestPO
	@ManyToOne
	@JoinColumn(name = "requestId")
	private EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest;

	// bi-directional many-to-one association to EFmFmClientBranchPO
	@ManyToOne
	@JoinColumn(name = "BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;
	
	

	public int getAlgoRouteId() {
		return algoRouteId;
	}

	public void setAlgoRouteId(int algoRouteId) {
		this.algoRouteId = algoRouteId;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	
	public boolean isAlgoRouteOregion() {
		return algoRouteOregion;
	}

	public void setAlgoRouteOregion(boolean algoRouteOregion) {
		this.algoRouteOregion = algoRouteOregion;
	}



	

	public boolean isRouteAllocationStatus() {
		return routeAllocationStatus;
	}

	public void setRouteAllocationStatus(boolean routeAllocationStatus) {
		this.routeAllocationStatus = routeAllocationStatus;
	}

	public AreaEmpClusterMappingPO getAreaEmpClusterMapping() {
		return AreaEmpClusterMapping;
	}

	public void setAreaEmpClusterMapping(AreaEmpClusterMappingPO areaEmpClusterMapping) {
		AreaEmpClusterMapping = areaEmpClusterMapping;
	}

	public EFmFmEmployeeTravelRequestPO geteFmFmEmployeeTravelRequest() {
		return eFmFmEmployeeTravelRequest;
	}

	public void seteFmFmEmployeeTravelRequest(EFmFmEmployeeTravelRequestPO eFmFmEmployeeTravelRequest) {
		this.eFmFmEmployeeTravelRequest = eFmFmEmployeeTravelRequest;
	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	public int getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(int vehicleType) {
		this.vehicleType = vehicleType;
	}

	

   
}
