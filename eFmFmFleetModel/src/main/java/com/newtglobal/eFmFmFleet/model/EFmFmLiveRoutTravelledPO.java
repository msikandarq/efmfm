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
@Table(name="eFmFmLiveRouteTravelled")
public class EFmFmLiveRoutTravelledPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="LiveRouteTravelId", length=15)	 
	private int liveRouteTravelId;
	

	@Column(name="LiveLatitudeLongitude", length=50)
	private String livelatitudeLongitude;

	@Column(name="LiveEta", length=50)
	private String liveEta;

	@Column(name="LiveTravellesDistance", length=50)
	private String liveTravellesDistance;

	
	@Column(name="LiveCurrentCabLocation", length=250)
	private String liveCurrentCabLocation;

	@Column(name="LiveSpeed", length=15)
	private String liveSpeed;
	
    @Transient
	private String tripType;
    
    @Transient
   	private String time;

    @Column(name="LiveEtaInSeconds", length=10)
	private int liveEtaInSeconds;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LiveTravelledTime", length=15)
	private Date liveTravelledTime;
		

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
	String travelTime;



	public EFmFmLiveRoutTravelledPO() {
	}



	public int getLiveRouteTravelId() {
		return liveRouteTravelId;
	}



	public void setLiveRouteTravelId(int liveRouteTravelId) {
		this.liveRouteTravelId = liveRouteTravelId;
	}



	public String getLivelatitudeLongitude() {
		return livelatitudeLongitude;
	}



	public void setLivelatitudeLongitude(String livelatitudeLongitude) {
		this.livelatitudeLongitude = livelatitudeLongitude;
	}



	public String getLiveEta() {
		return liveEta;
	}



	public void setLiveEta(String liveEta) {
		this.liveEta = liveEta;
	}



	public String getLiveTravellesDistance() {
		return liveTravellesDistance;
	}



	public void setLiveTravellesDistance(String liveTravellesDistance) {
		this.liveTravellesDistance = liveTravellesDistance;
	}



	public String getLiveCurrentCabLocation() {
		return liveCurrentCabLocation;
	}



	public void setLiveCurrentCabLocation(String liveCurrentCabLocation) {
		this.liveCurrentCabLocation = liveCurrentCabLocation;
	}



	public String getLiveSpeed() {
		return liveSpeed;
	}



	public void setLiveSpeed(String liveSpeed) {
		this.liveSpeed = liveSpeed;
	}



	public String getTripType() {
		return tripType;
	}



	public void setTripType(String tripType) {
		this.tripType = tripType;
	}



	public String getTime() {
		return time;
	}



	public void setTime(String time) {
		this.time = time;
	}



	public int getLiveEtaInSeconds() {
		return liveEtaInSeconds;
	}



	public void setLiveEtaInSeconds(int liveEtaInSeconds) {
		this.liveEtaInSeconds = liveEtaInSeconds;
	}



	public Date getLiveTravelledTime() {
		return liveTravelledTime;
	}



	public void setLiveTravelledTime(Date liveTravelledTime) {
		this.liveTravelledTime = liveTravelledTime;
	}



	public EFmFmAssignRoutePO getEfmFmAssignRoute() {
		return efmFmAssignRoute;
	}



	public void setEfmFmAssignRoute(EFmFmAssignRoutePO efmFmAssignRoute) {
		this.efmFmAssignRoute = efmFmAssignRoute;
	}



	public EFmFmClientBranchPO geteFmFmClientBranchPO() {
		return eFmFmClientBranchPO;
	}



	public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
		this.eFmFmClientBranchPO = eFmFmClientBranchPO;
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

	
}