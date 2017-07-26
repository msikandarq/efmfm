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

/**
 * The persistent class for the eFmFmCheckInVehicleTracking database table.
 * 
 */
@Entity
@Table(name="eFmFmCheckInVehicleTracking")
public class EFmCheckInVehicleTrackingPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CheckInVehicleTrackingId", length=15)
	private int checkInVehicleTrackingId;

	
	@Column(name="LatitudeLongitude", length=100)
    private String latitudeLongitude;
	
	
	@Column(name="CurrentCabLocation", length=250)
    private String currentCabLocation;
	
	@Column(name="speed", length=50)
    private String speed;
	
	@Column(name="Status", length=15)
    private String status;
	
	@Column(name="Address", length=255)
    private String address;
	
	@Column(name="trackingType", length=50)
    private String TrackingType;	
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="CurrentTime", length=15)
    private Date currentTime;
	
	//bi-directional many-to-one association to EFmFmVehicleCheckIn
	@ManyToOne
	@JoinColumn(name="CheckInId")
	private EFmFmVehicleCheckInPO efmFmVehicleCheckIn;
	
	
	//bi-directional many-to-one association to EFmFmClientMaster
	@ManyToOne
	@JoinColumn(name="BranchId")
	private EFmFmClientBranchPO eFmFmClientBranchPO;

	public EFmCheckInVehicleTrackingPO() {
	}

    public int getCheckInVehicleTrackingId() {
        return checkInVehicleTrackingId;
    }

    public void setCheckInVehicleTrackingId(int checkInVehicleTrackingId) {
        this.checkInVehicleTrackingId = checkInVehicleTrackingId;
    }

    public String getLatitudeLongitude() {
        return latitudeLongitude;
    }

    public void setLatitudeLongitude(String latitudeLongitude) {
        this.latitudeLongitude = latitudeLongitude;
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

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public EFmFmVehicleCheckInPO getEfmFmVehicleCheckIn() {
        return efmFmVehicleCheckIn;
    }

    public void setEfmFmVehicleCheckIn(EFmFmVehicleCheckInPO efmFmVehicleCheckIn) {
        this.efmFmVehicleCheckIn = efmFmVehicleCheckIn;
    }

    public EFmFmClientBranchPO geteFmFmClientBranchPO() {
        return eFmFmClientBranchPO;
    }

    public void seteFmFmClientBranchPO(EFmFmClientBranchPO eFmFmClientBranchPO) {
        this.eFmFmClientBranchPO = eFmFmClientBranchPO;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTrackingType() {
		return TrackingType;
	}

	public void setTrackingType(String trackingType) {
		TrackingType = trackingType;
	}
	
	
	
}