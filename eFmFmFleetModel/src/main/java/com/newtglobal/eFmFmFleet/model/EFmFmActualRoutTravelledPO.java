package com.newtglobal.eFmFmFleet.model;
import java.io.Serializable;
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
import javax.persistence.Transient;

/**
 * The persistent class for the eFmFm_Actual_Route_Travelled database table.
 * 
 */
@Entity
@Table(name="eFmFmActualRouteTravelled")
public class EFmFmActualRoutTravelledPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TravelId", length=15)	 
	private int travelId;
	

	@Column(name="LatitudeLongitude", length=50)
	private String latitudeLongitude;

	@Column(name="CurrentEta", length=50)
	private String currentEta;

	@Column(name="TravellesDistance", length=50)
	private String travelledDistance;

	
	@Column(name="CurrentCabLocation", length=250)
	private String currentCabLocation;

	@Column(name="speed", length=15)
	private String speed;
	
    @Transient
	private String tripType;
    
    @Transient
	private String sysUpdatedStatus;
    
    @Transient
	private int userId;	
    
    @Transient
   	private String time;

    @Column(name="EtaInSeconds", length=10)
	private int etaInSeconds;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TravelledTime", length=15)
	private Date travelledTime;
		

	@ManyToOne
	@JoinColumn(name="AssignRouteId")
	private EFmFmAssignRoutePO efmFmAssignRoute;
	
	//bi-directional many-to-one association to EFmFmClientMaster
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;
	
	@Transient
	long tripUpdateTime;
	
	@Transient
	int routeId;
	
	@Transient
	String travelTime;
	
	
	@Transient
	private String combinedFacility;



	public EFmFmActualRoutTravelledPO() {
	}

	public int getTravelId() {
		return this.travelId;
	}

	public void setTravelId(int travelId) {
		this.travelId = travelId;
	}

	public String getLatitudeLongitude() {
		return latitudeLongitude;
	}

	public void setLatitudeLongitude(String latitudeLongitude) {
		this.latitudeLongitude = latitudeLongitude;
	}

	public Date getTravelledTime() {
		return travelledTime;
	}

	public void setTravelledTime(Date travelledTime) {
		this.travelledTime = travelledTime;
	}

	public EFmFmAssignRoutePO getEfmFmAssignRoute() {
		return efmFmAssignRoute;
	}

	public void setEfmFmAssignRoute(EFmFmAssignRoutePO efmFmAssignRoute) {
		this.efmFmAssignRoute = efmFmAssignRoute;
	}

	public String getCurrentEta() {
		return currentEta;
	}
	
	

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public void setCurrentEta(String currentEta) {
		this.currentEta = currentEta;
	}

	public String getTravelledDistance() {
		return travelledDistance;
	}

	public void setTravelledDistance(String travelledDistance) {
		this.travelledDistance = travelledDistance;
	}

	public String getCurrentCabLocation() {
		return currentCabLocation;
	}

	public void setCurrentCabLocation(String currentCabLocation) {
		this.currentCabLocation = currentCabLocation;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}

	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
	}

	public int getEtaInSeconds() {
		return etaInSeconds;
	}

	public void setEtaInSeconds(int etaInSeconds) {
		this.etaInSeconds = etaInSeconds;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public long getTripUpdateTime() {
		return tripUpdateTime;
	}

	public void setTripUpdateTime(long tripUpdateTime) {
		this.tripUpdateTime = tripUpdateTime;
	}

	public String getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public String getSysUpdatedStatus() {
		return sysUpdatedStatus;
	}

	public void setSysUpdatedStatus(String sysUpdatedStatus) {
		this.sysUpdatedStatus = sysUpdatedStatus;
	}

	public String getCombinedFacility() {
		return combinedFacility;
	}

	public void setCombinedFacility(String combinedFacility) {
		this.combinedFacility = combinedFacility;
	}


	

	

}